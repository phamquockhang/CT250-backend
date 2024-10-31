package com.dvk.ct250backend.domain.booking.mapper;

import com.dvk.ct250backend.domain.booking.dto.BaggagePricingDTO;
import com.dvk.ct250backend.domain.booking.entity.BaggagePricing;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BaggagePricingMapper {
    @Mapping(target = "baggage", ignore = true)
    BaggagePricingDTO toBaggagePricingDTO(BaggagePricing baggagePricing);
    BaggagePricing toBaggagePricing(BaggagePricingDTO baggagePricingDTO);
    void updateBaggagePricingFromDTO(@MappingTarget BaggagePricing baggagePricing, BaggagePricingDTO baggagePricingDTO);
}
