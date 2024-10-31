package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.transaction.dto.TransactionDTO;
import com.dvk.ct250backend.domain.transaction.dto.request.VNPayCallbackRequest;
import com.dvk.ct250backend.domain.transaction.service.TransactionService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionController {
    TransactionService transactionService;

    @PostMapping
    public ApiResponse<TransactionDTO> createTransaction(HttpServletRequest request, @RequestBody TransactionDTO transactionDTO) throws ResourceNotFoundException {
        return ApiResponse.<TransactionDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(transactionService.createTransaction(request,transactionDTO))
                .build();
    }
    @GetMapping("/vn-pay-callback")
    public ApiResponse<TransactionDTO> payCallbackHandler(HttpServletRequest request) throws ResourceNotFoundException, MessagingException, UnsupportedEncodingException {
        VNPayCallbackRequest callbackRequest = new VNPayCallbackRequest();
        callbackRequest.setVnp_ResponseCode(request.getParameter("vnp_ResponseCode"));
        callbackRequest.setVnp_TransactionNo(request.getParameter("vnp_TransactionNo"));
        callbackRequest.setVnp_Amount(request.getParameter("vnp_Amount"));
        callbackRequest.setVnp_BankCode(request.getParameter("vnp_BankCode"));
        callbackRequest.setVnp_OrderInfo(request.getParameter("vnp_OrderInfo"));
        callbackRequest.setVnp_PayDate(request.getParameter("vnp_PayDate"));
        callbackRequest.setVnp_TxnRef(request.getParameter("vnp_TxnRef"));

        TransactionDTO transactionDTO = transactionService.handleVNPayCallback(callbackRequest);

        return ApiResponse.<TransactionDTO>builder()
                .status("00".equals(transactionDTO.getStatus()) ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value())
                .payload(transactionDTO)
                .build();
    }
}
