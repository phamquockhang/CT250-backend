package com.dvk.ct250backend.domain.flight.service;

import com.dvk.ct250backend.domain.flight.dto.FeeGroupDTO;

import java.util.List;

public interface FeeGroupService {
    List<FeeGroupDTO> getAllFeeGroups();
    FeeGroupDTO createFeeGroup(FeeGroupDTO feeGroupDTO);
}
