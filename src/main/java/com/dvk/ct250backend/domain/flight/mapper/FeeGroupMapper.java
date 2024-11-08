package com.dvk.ct250backend.domain.flight.mapper;

import com.dvk.ct250backend.domain.flight.dto.FeeGroupDTO;
import com.dvk.ct250backend.domain.flight.entity.FeeGroup;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FeeGroupMapper {
    FeeGroupDTO toFeeGroupDTO(FeeGroup feeGroup);
    FeeGroup toFeeGroup(FeeGroupDTO feeGroupDTO);
}
