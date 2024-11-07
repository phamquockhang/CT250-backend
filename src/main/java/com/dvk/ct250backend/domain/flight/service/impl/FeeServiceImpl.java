package com.dvk.ct250backend.domain.flight.service.impl;

import com.dvk.ct250backend.app.dto.response.Meta;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.flight.dto.FeeDTO;
import com.dvk.ct250backend.domain.flight.entity.Fee;
import com.dvk.ct250backend.domain.flight.entity.FeeGroup;
import com.dvk.ct250backend.domain.flight.mapper.FeeMapper;
import com.dvk.ct250backend.domain.flight.repository.FeeGroupRepository;
import com.dvk.ct250backend.domain.flight.repository.FeePricingRepository;
import com.dvk.ct250backend.domain.flight.repository.FeeRepository;
import com.dvk.ct250backend.domain.flight.service.FeeService;
import com.dvk.ct250backend.infrastructure.utils.RequestParamUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeeServiceImpl implements FeeService {

    FeeRepository feeRepository;
    FeeGroupRepository feeGroupRepository;
    FeePricingRepository feePricingRepository;
    RequestParamUtils requestParamUtils;
    FeeMapper feeMapper;


    @Override
    public Page<FeeDTO> getFees(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<Fee> feePage = feeRepository.findAll(pageable);
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(feePage.getTotalPages())
                .total(feePage.getTotalElements())
                .build();
        return Page.<FeeDTO>builder()
                .meta(meta)
                .content(feePage.getContent().stream()
                        .map(feeMapper::toFeeDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public FeeDTO createFee(FeeDTO feeDTO) {
        Fee fee = feeMapper.toFee(feeDTO);
        Fee savedFee = feeRepository.save(fee);
        return feeMapper.toFeeDTO(savedFee);
    }

    @Override
    @Transactional
    public FeeDTO updateFee(Integer feeId, FeeDTO feeDTO) throws ResourceNotFoundException {
        Fee fee = feeRepository.findById(feeId)
                .orElseThrow(() -> new ResourceNotFoundException("Fee not found"));
        if(!Objects.equals(feeDTO.getFeeGroup().getFeeGroupId(), fee.getFeeGroup().getFeeGroupId())) {
            FeeGroup feeGroup = feeGroupRepository.findById(feeDTO.getFeeGroup().getFeeGroupId())
                    .orElseThrow(() -> new ResourceNotFoundException("Fee group not found"));
            fee.setFeeGroup(feeGroup);
        }
        feeMapper.updateFeeFromDTO(fee, feeDTO);
        if (fee.getFeePricing() != null) {
            fee.getFeePricing().forEach(feePricing -> feePricing.setFee(Fee.builder().feeId(feeId).build()));
        }
        return feeMapper.toFeeDTO(feeRepository.save(fee));
    }
}
