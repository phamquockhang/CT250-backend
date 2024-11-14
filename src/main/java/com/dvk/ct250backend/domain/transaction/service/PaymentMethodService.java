package com.dvk.ct250backend.domain.transaction.service;

import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.transaction.dto.PaymentMethodDTO;

import java.util.List;
import java.util.Map;

public interface PaymentMethodService {
    PaymentMethodDTO createPaymentMethod(PaymentMethodDTO paymentMethodDTO);
    PaymentMethodDTO updatePaymentMethod(Integer id, PaymentMethodDTO paymentMethodDTO) throws ResourceNotFoundException;
    void deletePaymentMethod(Integer id) throws ResourceNotFoundException;
    Page<PaymentMethodDTO> getPaymentMethods(Map<String, String> params);
    List<PaymentMethodDTO> getAllPaymentMethods();
}
