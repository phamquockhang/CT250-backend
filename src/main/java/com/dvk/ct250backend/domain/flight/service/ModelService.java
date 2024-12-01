package com.dvk.ct250backend.domain.flight.service;

import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.flight.dto.ModelDTO;

import java.util.List;

public interface ModelService {
    List<ModelDTO> getAllModel();
    ModelDTO createModel(ModelDTO modelDTO);
    void deleteModel(Integer id) throws ResourceNotFoundException;
}
