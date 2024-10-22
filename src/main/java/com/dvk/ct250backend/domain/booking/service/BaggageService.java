package com.dvk.ct250backend.domain.booking.service;

import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.BaggageDTO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface BaggageService {
    BaggageDTO createBaggage(BaggageDTO BaggageDTO);
    BaggageDTO updateBaggage(Integer BaggageId, BaggageDTO BaggageDTO) throws ResourceNotFoundException;
    void deleteBaggage(Integer BaggageId) throws ResourceNotFoundException;
    List<BaggageDTO> getAllBaggage();
    Page<BaggageDTO> getBaggage(Map<String, String> params);
}
