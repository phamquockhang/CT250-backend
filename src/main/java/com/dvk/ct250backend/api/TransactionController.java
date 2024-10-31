package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.transaction.dto.TransactionDTO;
import com.dvk.ct250backend.domain.transaction.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
