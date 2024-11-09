package com.dvk.ct250backend.infrastructure.service;

import com.dvk.ct250backend.domain.auth.entity.User;
import com.dvk.ct250backend.domain.booking.entity.Booking;
import com.dvk.ct250backend.domain.booking.entity.BookingPassenger;
import com.dvk.ct250backend.domain.booking.repository.BookingRepository;
import com.dvk.ct250backend.domain.common.service.EmailService;
import com.dvk.ct250backend.infrastructure.kafka.mail.EmailKafkaProducer;
import com.dvk.ct250backend.infrastructure.kafka.mail.EmailMessage;
import com.dvk.ct250backend.infrastructure.kafka.pdf.PdfKafkaProducer;
import com.dvk.ct250backend.infrastructure.kafka.pdf.PdfMessage;
import com.dvk.ct250backend.infrastructure.utils.PdfGeneratorUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EmailServiceImpl implements EmailService {
    JavaMailSender mailSender;
    SpringTemplateEngine templateEngine;
    BookingRepository bookingRepository;
    EmailKafkaProducer emailKafkaProducer;
    String fromAddress = "davikaairways1109@gmail.com";
    String senderName = "DAVIKA AIRWAYS";
    private final PdfKafkaProducer pdfKafkaProducer;

    private void sendEmail(String toAddress, String subject, String templateName, Map<String, Object> context) throws MessagingException, UnsupportedEncodingException {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(context);
        String content = templateEngine.process(templateName, thymeleafContext);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    public CompletableFuture<Void> sendEmailAsync(String toAddress, String subject, String templateName, Map<String, Object> context) {
        return CompletableFuture.runAsync(() -> {
            try {
                sendEmail(toAddress, subject, templateName, context);
            } catch (MessagingException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void sendVerificationEmail(User user, String siteURL, String verifyToken)  {
        Map<String, Object> context = Map.of(
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "verifyURL", siteURL + "/verify?token=" + verifyToken
        );

        EmailMessage emailMessage = EmailMessage.builder()
                .toAddress(user.getEmail())
                .subject("Vui lòng xác nhận đăng ký của bạn")
                .templateName("verification-email")
                .context(context)
                .build();

        emailKafkaProducer.sendEmailEvent(emailMessage);
    }

    @Override
    public void sendPasswordResetEmail(User user, String resetURL) {
        Map<String, Object> context = Map.of(
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "resetURL", resetURL
        );

        EmailMessage emailMessage = EmailMessage.builder()
                .toAddress(user.getEmail())
                .subject("Yêu cầu đặt lại mật khẩu")
                .templateName("password-reset-email")
                .context(context)
                .build();

        emailKafkaProducer.sendEmailEvent(emailMessage);
    }

    @Override
    public void sendTemporaryBookingCodeEmail(String bookingCode, LocalDateTime paymentDeadline) {
        Booking booking = bookingRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found for code: " + bookingCode));

        String toAddress = booking.getBookingFlights().stream()
                .flatMap(bookingFlight -> bookingFlight.getBookingPassengers().stream())
                .filter(BookingPassenger::getIsPrimaryContact)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Primary contact not found"))
                .getPassenger()
                .getEmail();

        ZonedDateTime zonedPaymentDeadline = paymentDeadline.atZone(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM HH:mm (z)", new Locale("vi", "VN"));
        String formattedDeadline = zonedPaymentDeadline.format(formatter);
        Map<String, Object> context = Map.of(
                "bookingCode", bookingCode,
                "paymentDeadline", formattedDeadline
        );

        EmailMessage emailMessage = EmailMessage.builder()
                .toAddress(toAddress)
                .subject("Đặt Chỗ Tạm Thời Davika Airways")
                .templateName("verify-reserve-booking")
                .context(context)
                .build();

        emailKafkaProducer.sendEmailEvent(emailMessage);
    }

    @Override
    public void sendTicketConfirmationEmail(Booking booking) throws Exception {
        String toAddress = booking.getBookingFlights().stream()
                .flatMap(bookingFlight -> bookingFlight.getBookingPassengers().stream())
                .filter(BookingPassenger::getIsPrimaryContact)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Primary contact not found"))
                .getPassenger()
                .getEmail();
        String subject = generateSubject(booking);

        Context context = new Context();
        context.setVariable("booking", booking);

        String content = templateEngine.process("verify-booking-email", context);

        String pdfContent = templateEngine.process("ticket", context);
        byte[] pdfData = PdfGeneratorUtils.generateTicketPdf(pdfContent);

        PdfMessage pdfMessage = PdfMessage.builder()
                .bookingId(booking.getBookingId().toString())
                .context(Map.of(
                        "toAddress", toAddress,
                        "subject", subject,
                        "content", content,
                        "pdfData", pdfData
                ))
                .build();
        pdfKafkaProducer.sendPdfEvent(pdfMessage);
    }

    public void sendEmailWithAttachment(String toAddress, String subject, String content, File pdfFile) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.addAttachment("Vé Điện Tử.pdf", pdfFile);

            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Failed to send email with attachment", e);
        }
    }

    public CompletableFuture<Void> sendEmailWithAttachmentAsync(String toAddress, String subject, String content, File pdfFile) {
        return CompletableFuture.runAsync(() -> {
            sendEmailWithAttachment(toAddress, subject, content, pdfFile);
        });
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