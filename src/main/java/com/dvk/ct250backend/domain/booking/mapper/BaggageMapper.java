package com.dvk.ct250backend.domain.booking.mapper;

import com.dvk.ct250backend.domain.booking.dto.BaggageDTO;
import com.dvk.ct250backend.domain.booking.entity.Baggage;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {BaggagePricingMapper.class})
public interface BaggageMapper {
    BaggageDTO toBaggageDTO(Baggage baggage);
    Baggage toBaggage(BaggageDTO baggageDTO);
    void updateBaggageFromDTO(@MappingTarget Baggage baggage, BaggageDTO baggageDTO);
}
