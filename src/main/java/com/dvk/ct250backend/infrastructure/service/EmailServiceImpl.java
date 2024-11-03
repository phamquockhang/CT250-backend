package com.dvk.ct250backend.infrastructure.service;

import com.dvk.ct250backend.domain.auth.entity.User;
import com.dvk.ct250backend.domain.booking.entity.Booking;
import com.dvk.ct250backend.domain.booking.entity.BookingPassenger;
import com.dvk.ct250backend.domain.common.service.EmailService;
import com.dvk.ct250backend.infrastructure.utils.FileUtils;
import com.dvk.ct250backend.infrastructure.utils.PdfGeneratorUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailServiceImpl implements EmailService {
    JavaMailSender mailSender;
    SpringTemplateEngine templateEngine;
    FileUtils fileUtils;
    String fromAddress = "davikaairways1109@gmail.com";
    String senderName = "DAVIKA AIRWAYS";

    private void sendEmail(String toAddress, String subject, String templateName, Context context) throws MessagingException, UnsupportedEncodingException {
        String content = templateEngine.process(templateName, context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    @Override
    public void sendVerificationEmail(User user, String siteURL, String verifyToken) throws MessagingException, UnsupportedEncodingException {
        Context context = new Context();
        context.setVariable("firstName", user.getFirstName());
        context.setVariable("lastName", user.getLastName());
        context.setVariable("verifyURL", siteURL + "/verify?token=" + verifyToken);

        sendEmail(user.getEmail(), "Vui lòng xác nhận đăng ký của bạn", "verification-email", context);
    }

    @Override
    public void sendPasswordResetEmail(User user, String resetURL) throws MessagingException, UnsupportedEncodingException {
        Context context = new Context();
        context.setVariable("firstName", user.getFirstName());
        context.setVariable("lastName", user.getLastName());
        context.setVariable("resetURL", resetURL);

        sendEmail(user.getEmail(), "Yêu cầu đặt lại mật khẩu", "password-reset-email", context);
    }

    public void sendTicketConfirmationEmail(Booking booking) throws Exception {
        String toAddress = booking.getBookingFlights().stream()
                .flatMap(bookingFlight -> bookingFlight.getBookingPassengers().stream())
                .filter(BookingPassenger::getIsPrimaryContact)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Primary contact not found"))
                .getPassenger()
                .getEmail();
        //String subject = "Vé Điện Tử Davika Airways Của Quý Khách - Mã Đặt Chỗ " + booking.getBookingCode();
        String subject = generateSubject(booking);
        Context context = new Context();
        context.setVariable("booking", booking);

        String content = templateEngine.process("verify-booking-email", context);

        String pdfContent = templateEngine.process("ticket", context);
        byte[] pdfData = PdfGeneratorUtils.generateTicketPdf(pdfContent);

        File tempFile = fileUtils.saveTempFile(pdfData, "ticket.pdf");

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(content, true);
        helper.addAttachment("Vé Điện Tử.pdf", tempFile);

        mailSender.send(message);

        if (!tempFile.delete()) {
            System.err.println("Failed to delete temporary file: " + tempFile.getAbsolutePath());
        }
    }
    private String generateSubject(Booking booking) {
        BookingPassenger primaryContact = booking.getBookingFlights().stream()
                .flatMap(bookingFlight -> bookingFlight.getBookingPassengers().stream())
                .filter(BookingPassenger::getIsPrimaryContact)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Primary contact not found"));

        String passengerName = primaryContact.getPassenger().getFirstName() + " " + primaryContact.getPassenger().getLastName();
        String destination = booking.getBookingFlights().stream()
                .map(bookingFlight -> bookingFlight.getFlight().getRoute().getArrivalAirport().getAirportName() + ", " + bookingFlight.getFlight().getRoute().getArrivalAirport().getCityName())
                .findFirst()
                .orElse("Unknown Destination");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'Ngày' dd 'Tháng' MM");
        String departureDate = booking.getBookingFlights().stream()
                .map(bookingFlight -> bookingFlight.getFlight().getDepartureDateTime().toLocalDate().format(formatter))
                .findFirst()
                .orElse("Unknown Date");

        return "Đặt chỗ " + departureDate + " tới " + destination + " của " + passengerName;
    }
}