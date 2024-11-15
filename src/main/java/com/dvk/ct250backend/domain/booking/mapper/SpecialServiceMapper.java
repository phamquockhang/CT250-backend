package com.dvk.ct250backend.domain.booking.mapper;

import com.dvk.ct250backend.domain.booking.dto.SpecialServiceDTO;
import com.dvk.ct250backend.domain.booking.entity.SpecialService;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {SpecialServiceMapper.class})
public interface SpecialServiceMapper {
    SpecialServiceDTO toSpecialServiceDTO(SpecialService specialService);
    SpecialService toSpecialService(SpecialServiceDTO specialServiceDTO);
    void updateSpecialServiceFromDTO(@MappingTarget SpecialService specialService, SpecialServiceDTO specialServiceDTO);
}
