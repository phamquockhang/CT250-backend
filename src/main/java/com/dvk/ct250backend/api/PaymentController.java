package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.PaymentDTO;
import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.infrastructure.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {

    PaymentService paymentService;
    @GetMapping("/vn-pay")
    public ApiResponse<PaymentDTO.VNPayResponse> pay(HttpServletRequest request) {
        return ApiResponse.<PaymentDTO.VNPayResponse>builder()
                .status(HttpStatus.OK.value())
                .payload(paymentService.createVnPayPayment(request))
                .build();
    }
}
