package com.dvk.ct250backend.domain.transaction.service;

import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.transaction.dto.response.VNPayResponse;
import com.dvk.ct250backend.domain.transaction.entity.Transaction;
import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {
    VNPayResponse createVnPayPayment(HttpServletRequest request, Transaction transaction, String txnRef) throws ResourceNotFoundException;
}
