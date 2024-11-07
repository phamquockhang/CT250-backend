package com.dvk.ct250backend.domain.flight.service.impl;

import com.dvk.ct250backend.domain.flight.dto.FeeGroupDTO;
import com.dvk.ct250backend.domain.flight.mapper.FeeGroupMapper;
import com.dvk.ct250backend.domain.flight.repository.FeeGroupRepository;
import com.dvk.ct250backend.domain.flight.service.FeeGroupService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeeGroupServiceImpl implements FeeGroupService {

    FeeGroupRepository feeGroupRepository;
    FeeGroupMapper feeGroupMapper;

    @Override
    public List<FeeGroupDTO> getAllFeeGroups() {
        return feeGroupRepository.findAll().stream()
                .map(feeGroupMapper::toFeeGroupDTO)
                .toList();
    }
}
