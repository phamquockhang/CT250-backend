package com.dvk.ct250backend.domain.booking.service.impl;

import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.BaggagePricingDTO;
import com.dvk.ct250backend.domain.booking.entity.BaggagePricing;
import com.dvk.ct250backend.domain.booking.mapper.BaggagePricingMapper;
import com.dvk.ct250backend.domain.booking.repository.BaggagePricingRepository;
import com.dvk.ct250backend.domain.booking.service.BaggagePricingService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class BaggagePricingServiceImpl implements BaggagePricingService {
    BaggagePricingRepository baggagePricingRepository;
    BaggagePricingMapper baggagePricingMapper;
    @Override
    public BaggagePricingDTO createBaggagePricing(BaggagePricingDTO baggagePricingDTO) {
        BaggagePricing baggagePricing = baggagePricingMapper.toBaggagePricing(baggagePricingDTO);
        return baggagePricingMapper.toBaggagePricingDTO(baggagePricingRepository.save(baggagePricing));
    }

    @Override
    public BaggagePricingDTO updateBaggagePricing(Integer baggagePricingId, BaggagePricingDTO baggagePricingDTO) throws ResourceNotFoundException {
        BaggagePricing baggagePricing = baggagePricingRepository.findById(baggagePricingId).orElseThrow(() -> new ResourceNotFoundException("BaggagePricing not found"));
        baggagePricingMapper.updateBaggagePricingFromDTO(baggagePricing, baggagePricingDTO);
        return baggagePricingMapper.toBaggagePricingDTO(baggagePricingRepository.save(baggagePricing));
    }

    @Override
    public void deleteBaggagePricing(Integer baggagePricingId) throws ResourceNotFoundException {
        BaggagePricing baggagePricing = baggagePricingRepository.findById(baggagePricingId).orElseThrow(() -> new ResourceNotFoundException("BaggagePricing not found"));
        baggagePricingRepository.delete(baggagePricing);
    }

    @Override
    public List<BaggagePricingDTO> getAllBaggagePricings() {
        return baggagePricingRepository.findAll().stream().map(baggagePricingMapper::toBaggagePricingDTO).collect(Collectors.toList());
    }
}
