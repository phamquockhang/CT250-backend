//package com.dvk.ct250backend.infrastructure.service.impl;
//
//import com.dvk.ct250backend.domain.auth.entity.User;
//import com.dvk.ct250backend.domain.booking.entity.Booking;
//import com.dvk.ct250backend.domain.booking.entity.BookingPassenger;
//import com.dvk.ct250backend.domain.common.service.EmailService;
//import com.dvk.ct250backend.infrastructure.service.RedisService;
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//
//import java.io.UnsupportedEncodingException;
//
//@Service
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class EmailServiceImpl implements EmailService {
//    JavaMailSender mailSender;
//    RedisService redisService;
//
//    String fromAddress = "davikaairways1109@gmail.com";
//    String senderName = "DAVIKA AIRWAYS";
//    @Override
//    public void sendVerificationEmail(User user, String siteURL, String verifyToken) throws MessagingException, UnsupportedEncodingException {
//        String toAddress = user.getEmail();
////        String fromAddress = "davikaairways1109@gmail.com";
////        String senderName = "DAVIKA AIRWAYS";
//        String subject = "Please verify your registration";
//
//        String content = "<html>"
//                + "<head>"
//                + "<style>"
//                + "body { font-family: 'Helvetica Neue', Arial, sans-serif; background-color: #e9ecef; margin: 0; padding: 0; }"
//                + "</style>"
//                + "</head>"
//                + "<body style='margin: 0; padding: 0; background-color: #e9ecef; font-family: Helvetica, Arial, sans-serif;'>"
//                + "<div style='width: 100%; max-width: 600px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);'>"
//                + "<div style='text-align: center; background: linear-gradient(90deg, #0066cc, #0099ff); color: white; padding: 30px; border-radius: 10px 10px 0 0;'>"
//                + "<h2 style='margin: 0; font-size: 24px;'>Welcome to DaViKa Airways!</h2>"
//                + "</div>"
//                + "<div style='margin: 20px; color: #333; line-height: 1.5;'>"
//                + "<p>Dear " + user.getFirstName() + ",</p>"
//                + "<p>Thank you for registering with DaViKa Airways! We're excited to have you on board.</p>"
//                + "<p>To complete your registration and activate your account, please confirm your email address by clicking the link below:</p>"
//                + "<a href='" + siteURL + "/verify?token=" + verifyToken + "' "
//                + "style='display: inline-block; padding: 15px 30px; background-color: #0056b3; color: white; text-decoration: none; "
//                + "border-radius: 5px; font-weight: bold; transition: background-color 0.3s, transform 0.2s; text-align: center;'>"
//                + "Activate Your Account</a>"
//                + "<div style='height: 1px; background-color: #e0e0e0; margin: 20px 0;'></div>"
//                + "<p>If you did not create an account, you can safely ignore this email.</p>"
//                + "<p>Thank you,<br>DaViKa Airways Team</p>"
//                + "</div>"
//                + "<div style='text-align: center; margin-top: 20px; font-size: 12px; color: #777;'>"
//                + "<p>&copy; " + java.time.Year.now().getValue() + " DaViKa Airways. All rights reserved.</p>"
//                + "</div>"
//                + "</div>"
//                + "</body>"
//                + "</html>";
//
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message);
//
//        helper.setFrom(fromAddress, senderName);
//        helper.setTo(toAddress);
//        helper.setSubject(subject);
//
//        content = content.replace("[[name]]", user.getFirstName());
//        String verifyURL = siteURL + "/verify?token=" + redisService.get(verifyToken);
//
//        content = content.replace("[[URL]]", verifyURL);
//
//        helper.setText(content, true);
//
//        mailSender.send(message);
//    }
//
//
//    @Override
//    public void sendPasswordResetEmail(User user, String resetURL) throws MessagingException, UnsupportedEncodingException {
//        String toAddress = user.getEmail();
//        String subject = "Password Reset Request";
//
//        String content = "<html>"
//                + "<head>"
//                + "<style>"
//                + "body { font-family: 'Helvetica Neue', Arial, sans-serif; background-color: #e9ecef; margin: 0; padding: 0; }"
//                + "</style>"
//                + "</head>"
//                + "<body style='margin: 0; padding: 0; background-color: #e9ecef; font-family: Helvetica, Arial, sans-serif;'>"
//                + "<div style='width: 100%; max-width: 600px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);'>"
//                + "<div style='text-align: center; background: linear-gradient(90deg, #0066cc, #0099ff); color: white; padding: 30px; border-radius: 10px 10px 0 0;'>"
//                + "<h2 style='margin: 0; font-size: 24px;'>Password Reset Request</h2>"
//                + "</div>"
//                + "<div style='margin: 20px; color: #333; line-height: 1.5;'>"
//                + "<p>Dear " + user.getFirstName() + ",</p>"
//                + "<p>We received a request to reset your password. Click the link below to reset your password:</p>"
//                + "<a href='" + resetURL + "' "
//                + "style='display: inline-block; padding: 15px 30px; background-color: #0056b3; color: white; text-decoration: none; "
//                + "border-radius: 5px; font-weight: bold; transition: background-color 0.3s, transform 0.2s; text-align: center;'>"
//                + "Reset Password</a>"
//                + "<div style='height: 1px; background-color: #e0e0e0; margin: 20px 0;'></div>"
//                + "<p>If you did not request a password reset, please ignore this email.</p>"
//                + "<p>Thank you,<br>DaViKa Airways Team</p>"
//                + "</div>"
//                + "<div style='text-align: center; margin-top: 20px; font-size: 12px; color: #777;'>"
//                + "<p>&copy; " + java.time.Year.now().getValue() + " DaViKa Airways. All rights reserved.</p>"
//                + "</div>"
//                + "</div>"
//                + "</body>"
//                + "</html>";
//
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message);
//
//        helper.setFrom(fromAddress, senderName);
//        helper.setTo(toAddress);
//        helper.setSubject(subject);
//
//        helper.setText(content, true);
//
//        mailSender.send(message);
//    }
//
//    @Override
//    public void sendTicketConfirmationEmail(Booking booking) throws MessagingException, UnsupportedEncodingException {
//        String toAddress = booking.getBookingFlights().stream()
//                .flatMap(bookingFlight -> bookingFlight.getBookingPassengers().stream())
//                .filter(BookingPassenger::getIsPrimaryContact)
//                .findFirst()
//                .orElseThrow(() -> new IllegalArgumentException("Primary contact not found"))
//                .getPassenger()
//                .getEmail();
//        String subject = "Vé Điện Tử Davika Airways Của Quý Khách - Mã Đặt Chỗ " + booking.getBookingCode();
//
//        String content = "<html>"
//                + "<head>"
//                + "<style>"
//                + "body { font-family: 'Helvetica Neue', Arial, sans-serif; background-color: #e9ecef; margin: 0; padding: 0; }"
//                + "</style>"
//                + "</head>"
//                + "<body style='margin: 0; padding: 0; background-color: #e9ecef; font-family: Helvetica, Arial, sans-serif;'>"
//                + "<div style='width: 100%; max-width: 600px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);'>"
//                + "<div style='text-align: center; background: linear-gradient(90deg, #0066cc, #0099ff); color: white; padding: 30px; border-radius: 10px 10px 0 0;'>"
//                + "<h2 style='margin: 0; font-size: 24px;'>Vé Điện Tử Vietravel Airlines Của Quý Khách</h2>"
//                + "<h3 style='margin: 0; font-size: 20px;'>Mã Đặt Chỗ " + booking.getBookingCode() + "</h3>"
//                + "</div>"
//                + "<div style='margin: 20px; color: #333; line-height: 1.5;'>"
//                + "<p>Kính gửi quý khách " + booking.getBookingFlights().stream()
//                .flatMap(bookingFlight -> bookingFlight.getBookingPassengers().stream())
//                .filter(BookingPassenger::getIsPrimaryContact)
//                .findFirst()
//                .orElseThrow(() -> new IllegalArgumentException("Primary contact not found"))
//                .getPassenger()
//                .getFirstName() + " " + booking.getBookingFlights().stream()
//                .flatMap(bookingFlight -> bookingFlight.getBookingPassengers().stream())
//                .filter(BookingPassenger::getIsPrimaryContact)
//                .findFirst()
//                .orElseThrow(() -> new IllegalArgumentException("Primary contact not found"))
//                .getPassenger()
//                .getLastName() + ",</p>"
//                + "<p>Yêu cầu đặt vé của quý khách đã được xác nhận thành công. Quý khách vui lòng xem vé điện tử trong tập tin đính kèm.</p>"
//                + "</div>"
//                + "<div style='text-align: center; margin-top: 20px; font-size: 12px; color: #777;'>"
//                + "<p>&copy; " + java.time.Year.now().getValue() + " Vietravel Airlines. All rights reserved.</p>"
//                + "</div>"
//                + "</div>"
//                + "</body>"
//                + "</html>";
//
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//
//        helper.setFrom(fromAddress, senderName);
//        helper.setTo(toAddress);
//        helper.setSubject(subject);
//        helper.setText(content, true);
//
//        // Attach the ticket file (assuming it's generated and available as a PDF)
//        // FileSystemResource file = new FileSystemResource(new File("path/to/ticket.pdf"));
//        // helper.addAttachment("Vé Điện Tử.pdf", file);
//
//        mailSender.send(message);
//    }
//
//}

package com.dvk.ct250backend.infrastructure.service;

import com.dvk.ct250backend.domain.auth.entity.User;
import com.dvk.ct250backend.domain.booking.entity.Booking;
import com.dvk.ct250backend.domain.booking.entity.BookingPassenger;
import com.dvk.ct250backend.domain.common.service.EmailService;
import com.dvk.ct250backend.domain.common.service.RedisService;
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

import java.io.*;
import java.util.HashMap;

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
        String subject = "Vé Điện Tử Davika Airways Của Quý Khách - Mã Đặt Chỗ " + booking.getBookingCode();

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
}