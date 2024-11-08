package com.dvk.ct250backend.domain.flight.mapper;

import com.dvk.ct250backend.domain.flight.dto.FeeDTO;
import com.dvk.ct250backend.domain.flight.entity.Fee;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {FeePricingMapper.class})
public interface FeeMapper {
    FeeDTO toFeeDTO(Fee fee);
    Fee toFee(FeeDTO feeDTO);
    void updateFeeFromDTO(@MappingTarget Fee fee, FeeDTO feeDTO);
}
