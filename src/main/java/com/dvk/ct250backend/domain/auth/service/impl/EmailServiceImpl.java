package com.dvk.ct250backend.domain.auth.service.impl;

import com.dvk.ct250backend.domain.auth.entity.User;
import com.dvk.ct250backend.domain.auth.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailServiceImpl implements EmailService {
    JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "davikaairways1109@gmail.com";
        String senderName = "DAVIKA AIRWAYS";
        String subject = "Please verify your registration";

        String content = "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: 'Helvetica Neue', Arial, sans-serif; background-color: #e9ecef; margin: 0; padding: 0; }"
                + "</style>"
                + "</head>"
                + "<body style='margin: 0; padding: 0; background-color: #e9ecef; font-family: Helvetica, Arial, sans-serif;'>"
                + "<div style='width: 100%; max-width: 600px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);'>"
                + "<div style='text-align: center; background: linear-gradient(90deg, #0066cc, #0099ff); color: white; padding: 30px; border-radius: 10px 10px 0 0;'>"
                + "<h2 style='margin: 0; font-size: 24px;'>Welcome to DaViKa Airways!</h2>"
                + "</div>"
                + "<div style='margin: 20px; color: #333; line-height: 1.5;'>"
                + "<p>Dear " + user.getFirstName() + ",</p>"
                + "<p>Thank you for registering with DaViKa Airways! We're excited to have you on board.</p>"
                + "<p>To complete your registration and activate your account, please confirm your email address by clicking the link below:</p>"
                + "<a href='" + siteURL + "/verify?token=" + user.getVerificationToken() + "' "
                + "style='display: inline-block; padding: 15px 30px; background-color: #0056b3; color: white; text-decoration: none; "
                + "border-radius: 5px; font-weight: bold; transition: background-color 0.3s, transform 0.2s; text-align: center;'>"
                + "Activate Your Account</a>"
                + "<div style='height: 1px; background-color: #e0e0e0; margin: 20px 0;'></div>"
                + "<p>If you did not create an account, you can safely ignore this email.</p>"
                + "<p>Thank you,<br>DaViKa Airways Team</p>"
                + "</div>"
                + "<div style='text-align: center; margin-top: 20px; font-size: 12px; color: #777;'>"
                + "<p>&copy; " + java.time.Year.now().getValue() + " DaViKa Airways. All rights reserved.</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFirstName());
        String verifyURL = siteURL + "/verify?token=" + user.getVerificationToken();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }


    public void sendPasswordResetEmail(User user, String resetURL) throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "davikaairways1109@gmail.com";
        String senderName = "DAVIKA AIRWAYS";
        String subject = "Password Reset Request";

        String content = "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: 'Helvetica Neue', Arial, sans-serif; background-color: #e9ecef; margin: 0; padding: 0; }"
                + "</style>"
                + "</head>"
                + "<body style='margin: 0; padding: 0; background-color: #e9ecef; font-family: Helvetica, Arial, sans-serif;'>"
                + "<div style='width: 100%; max-width: 600px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);'>"
                + "<div style='text-align: center; background: linear-gradient(90deg, #0066cc, #0099ff); color: white; padding: 30px; border-radius: 10px 10px 0 0;'>"
                + "<h2 style='margin: 0; font-size: 24px;'>Password Reset Request</h2>"
                + "</div>"
                + "<div style='margin: 20px; color: #333; line-height: 1.5;'>"
                + "<p>Dear " + user.getFirstName() + ",</p>"
                + "<p>We received a request to reset your password. Click the link below to reset your password:</p>"
                + "<a href='" + resetURL + "' "
                + "style='display: inline-block; padding: 15px 30px; background-color: #0056b3; color: white; text-decoration: none; "
                + "border-radius: 5px; font-weight: bold; transition: background-color 0.3s, transform 0.2s; text-align: center;'>"
                + "Reset Password</a>"
                + "<div style='height: 1px; background-color: #e0e0e0; margin: 20px 0;'></div>"
                + "<p>If you did not request a password reset, please ignore this email.</p>"
                + "<p>Thank you,<br>DaViKa Airways Team</p>"
                + "</div>"
                + "<div style='text-align: center; margin-top: 20px; font-size: 12px; color: #777;'>"
                + "<p>&copy; " + java.time.Year.now().getValue() + " DaViKa Airways. All rights reserved.</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

}
