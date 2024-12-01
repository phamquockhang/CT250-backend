package com.dvk.ct250backend.domain.flight.mapper;

import com.dvk.ct250backend.domain.flight.dto.ModelDTO;
import com.dvk.ct250backend.domain.flight.entity.Model;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ModelMapper {
    ModelDTO toModelDTO(Model model);
    Model toModel(ModelDTO modelDTO);
}
