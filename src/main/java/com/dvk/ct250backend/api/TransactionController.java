package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.transaction.dto.TransactionDTO;
import com.dvk.ct250backend.domain.transaction.dto.request.VNPayCallbackRequest;
import com.dvk.ct250backend.domain.transaction.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionController {
    TransactionService transactionService;
    Environment env;

    @GetMapping("/{id}")
    public ApiResponse<TransactionDTO> getTransactionById(@PathVariable Integer id) throws ResourceNotFoundException {
        return ApiResponse.<TransactionDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(transactionService.getTransactionById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<TransactionDTO> createTransaction(HttpServletRequest request, @RequestBody TransactionDTO transactionDTO) throws ResourceNotFoundException {
        return ApiResponse.<TransactionDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(transactionService.createTransaction(request,transactionDTO))
                .build();
    }

    @GetMapping("/vn-pay-callback")
    public ApiResponse<TransactionDTO> payCallbackHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        VNPayCallbackRequest callbackRequest = new VNPayCallbackRequest();
        callbackRequest.setVnp_ResponseCode(request.getParameter("vnp_ResponseCode"));
        callbackRequest.setVnp_TransactionNo(request.getParameter("vnp_TransactionNo"));
        callbackRequest.setVnp_Amount(request.getParameter("vnp_Amount"));
        callbackRequest.setVnp_BankCode(request.getParameter("vnp_BankCode"));
        callbackRequest.setVnp_OrderInfo(request.getParameter("vnp_OrderInfo"));
        callbackRequest.setVnp_PayDate(request.getParameter("vnp_PayDate"));
        callbackRequest.setVnp_TxnRef(request.getParameter("vnp_TxnRef"));

        final String FRONTEND_URL = env.getProperty("FRONTEND_URL");
        TransactionDTO transactionDTO = transactionService.handleVNPayCallback(callbackRequest);
        response.sendRedirect(FRONTEND_URL + "/book/payment/success?transactionId=" + transactionDTO.getTransactionId());

        return ApiResponse.<TransactionDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(transactionDTO)
                .build();
    }

    @GetMapping
    public ApiResponse<Page<TransactionDTO>> getTransactions(@RequestParam Map<String, String> params){
        return ApiResponse.<Page<TransactionDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(transactionService.getAllTransactions(params))
                .build();
    }

}
