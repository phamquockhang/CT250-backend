package com.dvk.ct250backend.domain.flight.mapper;

import com.dvk.ct250backend.domain.flight.dto.FeeDTO;
import com.dvk.ct250backend.domain.flight.entity.Fee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FeeMapper {
    FeeDTO toFeeDTO(Fee fee);
    Fee toFee(FeeDTO feeDTO);
}
