package com.dvk.ct250backend.domain.flight.mapper;

import com.dvk.ct250backend.domain.flight.dto.FeePricingDTO;
import com.dvk.ct250backend.domain.flight.entity.FeePricing;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FeePricingMapper {
    FeePricingDTO toFeePricingDTO(FeePricing feePricing);
    FeePricing toFeePricing(FeePricingDTO feePricingDTO);
    void updateFeePricingFromDTO(@MappingTarget FeePricing feePricing, FeePricingDTO feePricingDTO);
}
