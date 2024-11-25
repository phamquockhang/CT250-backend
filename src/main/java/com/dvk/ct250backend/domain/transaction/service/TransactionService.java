package com.dvk.ct250backend.domain.transaction.service;

import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.transaction.dto.TransactionDTO;
import com.dvk.ct250backend.domain.transaction.dto.request.VNPayCallbackRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface TransactionService {
    TransactionDTO createTransaction(HttpServletRequest request, TransactionDTO transactionDTO) throws ResourceNotFoundException;
    TransactionDTO handleVNPayCallback(VNPayCallbackRequest request) throws Exception;
    TransactionDTO getTransactionById(Integer transactionId);
    Page<TransactionDTO> getAllTransactions(Map<String, String> params);
}
