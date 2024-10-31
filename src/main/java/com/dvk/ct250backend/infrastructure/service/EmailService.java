package com.dvk.ct250backend.infrastructure.service;

import com.dvk.ct250backend.domain.auth.entity.User;
import com.dvk.ct250backend.domain.booking.entity.Booking;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface EmailService {
    void sendVerificationEmail(User user, String siteURL, String verifyToken) throws MessagingException, UnsupportedEncodingException;
    void sendPasswordResetEmail(User user, String resetURL) throws MessagingException, UnsupportedEncodingException;
    void sendTicketConfirmationEmail(Booking booking) throws MessagingException, UnsupportedEncodingException;
}
