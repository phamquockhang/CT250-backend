package com.dvk.ct250backend.domain.transaction.service.impl;

import com.dvk.ct250backend.domain.transaction.service.PaymentMethodService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentMethodServiceImpl implements PaymentMethodService {
}
