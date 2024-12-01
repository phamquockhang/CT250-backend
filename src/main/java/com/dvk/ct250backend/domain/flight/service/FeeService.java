package com.dvk.ct250backend.domain.flight.service;

import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.flight.dto.FeeDTO;

import java.util.Map;

public interface FeeService {
    Page<FeeDTO> getFees(Map<String, String> params);
    FeeDTO createFee(FeeDTO feeDTO);
    FeeDTO updateFee(Integer feeId, FeeDTO feeDTO) throws ResourceNotFoundException;
    FeeDTO getFeeById(Integer feeId) throws ResourceNotFoundException;
}
