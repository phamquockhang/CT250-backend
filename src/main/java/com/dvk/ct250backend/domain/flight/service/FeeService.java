package com.dvk.ct250backend.domain.flight.service;

import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.domain.flight.dto.FeeDTO;

import java.util.Map;

public interface FeeService {
    Page<FeeDTO> getFees(Map<String, String> params);
}
