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

    @GetMapping("/vn-pay-callback")
    public ApiResponse<PaymentDTO.VNPayResponse> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        String transactionNo = request.getParameter("vnp_TransactionNo");
        String amount = request.getParameter("vnp_Amount");
        String bankCode = request.getParameter("vnp_BankCode");
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String payDate = request.getParameter("vnp_PayDate");

        PaymentDTO.VNPayResponse.VNPayResponseBuilder responseBuilder = PaymentDTO.VNPayResponse.builder()
                .code(status)
                .message("00".equals(status) ? "Success" : "Failed")
                .paymentUrl("");

        if ("00".equals(status)) {
            responseBuilder
                    .transactionNo(transactionNo)
                    .amount(amount)
                    .bankCode(bankCode)
                    .orderInfo(orderInfo)
                    .payDate(payDate);
        }

        return ApiResponse.<PaymentDTO.VNPayResponse>builder()
                .status("00".equals(status) ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value())
                .payload(responseBuilder.build())
                .build();
    }
}
