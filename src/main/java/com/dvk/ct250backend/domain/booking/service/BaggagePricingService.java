package com.dvk.ct250backend.domain.booking.service;

import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.BaggagePricingDTO;

import java.util.List;

public interface BaggagePricingService {
    BaggagePricingDTO createBaggagePricing(BaggagePricingDTO baggagePricingDTO);
    BaggagePricingDTO updateBaggagePricing(Integer baggagePricingId, BaggagePricingDTO baggagePricingDTO) throws ResourceNotFoundException;
    void deleteBaggagePricing(Integer baggagePricingId) throws ResourceNotFoundException;
    List<BaggagePricingDTO> getAllBaggagePricings();
}
