package com.dvk.ct250backend.domain.booking.service;

import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.SpecialServiceDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SpecialServiceInterface {
    SpecialServiceDTO createSpecialService(SpecialServiceDTO specialServiceDTO, MultipartFile imgUrl) throws IOException;
    SpecialServiceDTO updateSpecialService(Integer specialServiceId, SpecialServiceDTO specialServiceDTO, MultipartFile imgUrl) throws IOException, ResourceNotFoundException;
    void deleteSpecialService(Integer specialServiceId) throws ResourceNotFoundException;
    List<SpecialServiceDTO> getAllSpecialServices();
    Page<SpecialServiceDTO> getSpecialServices(Map<String, String> params);
}
