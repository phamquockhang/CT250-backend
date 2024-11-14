package com.dvk.ct250backend.domain.transaction.mapper;

import com.dvk.ct250backend.domain.transaction.dto.PaymentMethodDTO;
import com.dvk.ct250backend.domain.transaction.entity.PaymentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {PaymentMethodMapper.class})
public interface PaymentMethodMapper {
    PaymentMethodDTO toPaymentMethodDTO(PaymentMethod paymentMethod);
    PaymentMethod toPaymentMethod(PaymentMethodDTO paymentMethodDTO);
    void updatePaymentMethodFromDTO(@MappingTarget PaymentMethod paymentMethod, PaymentMethodDTO paymentMethodDTO);
}
