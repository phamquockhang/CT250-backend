package com.dvk.ct250backend.domain.flight.service.impl;

import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.flight.dto.ModelDTO;
import com.dvk.ct250backend.domain.flight.mapper.ModelMapper;
import com.dvk.ct250backend.domain.flight.repository.ModelRepository;
import com.dvk.ct250backend.domain.flight.service.ModelService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ModelServiceImpl implements ModelService {
   ModelMapper modelMapper;
   ModelRepository modelRepository;

    @Override
    public List<ModelDTO> getAllModel() {
        return modelRepository.findAll().stream().map(modelMapper::toModelDTO).collect(Collectors.toList());
    }

    @Override
    public ModelDTO createModel(ModelDTO modelDTO) {
        return modelMapper.toModelDTO(modelRepository.save(modelMapper.toModel(modelDTO)));
    }

    @Override
    public void deleteModel(Integer id) throws ResourceNotFoundException {
        ModelDTO modelDTO = modelMapper.toModelDTO(modelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Model not found")));
        modelRepository.delete(modelMapper.toModel(modelDTO));
    }
}
