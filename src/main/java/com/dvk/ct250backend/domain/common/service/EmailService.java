package com.dvk.ct250backend.domain.common.service;

import com.dvk.ct250backend.domain.auth.entity.User;
import com.dvk.ct250backend.domain.booking.entity.Booking;

import java.time.LocalDateTime;

public interface EmailService {
    void sendVerificationEmail(User user, String siteURL, String verifyToken) ;
    void sendPasswordResetEmail(User user, String resetURL) ;
    void sendTicketConfirmationEmail(Booking booking) throws Exception;
    void sendTemporaryBookingCodeEmail(String bookingCode, LocalDateTime paymentDeadline);
}
