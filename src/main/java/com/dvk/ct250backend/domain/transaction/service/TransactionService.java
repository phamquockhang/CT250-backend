package com.dvk.ct250backend.domain.transaction.service;

import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.transaction.dto.TransactionDTO;
import com.dvk.ct250backend.domain.transaction.dto.request.VNPayCallbackRequest;
import com.dvk.ct250backend.domain.transaction.dto.response.VNPayResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface TransactionService {
    TransactionDTO createTransaction(HttpServletRequest request, TransactionDTO transactionDTO) throws ResourceNotFoundException;
    TransactionDTO handleVNPayCallback(VNPayCallbackRequest request) throws ResourceNotFoundException;
}
