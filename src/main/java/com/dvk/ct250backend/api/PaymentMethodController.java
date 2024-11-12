package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.transaction.dto.PaymentMethodDTO;
import com.dvk.ct250backend.domain.transaction.service.PaymentMethodService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment-methods")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentMethodController {

    PaymentMethodService paymentMethodService;

    @GetMapping
    public ApiResponse<Page<PaymentMethodDTO>> getPaymentMethods(@RequestParam Map<String, String> params){
        return ApiResponse.<Page<PaymentMethodDTO>>builder()
                .status(200)
                .payload(paymentMethodService.getPaymentMethods(params))
                .build();
    }

    @PostMapping
    public ApiResponse<PaymentMethodDTO> createPaymentMethod(@RequestBody PaymentMethodDTO paymentMethodDTO){
        return ApiResponse.<PaymentMethodDTO>builder()
                .status(HttpStatus.CREATED.value())
                .payload(paymentMethodService.createPaymentMethod(paymentMethodDTO))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<PaymentMethodDTO> updatePaymentMethod(@PathVariable Integer id, @RequestBody PaymentMethodDTO paymentMethodDTO) throws ResourceNotFoundException {
        return ApiResponse.<PaymentMethodDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(paymentMethodService.updatePaymentMethod(id, paymentMethodDTO))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePaymentMethod(@PathVariable Integer id) throws ResourceNotFoundException {
        paymentMethodService.deletePaymentMethod(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .build();
    }
}
