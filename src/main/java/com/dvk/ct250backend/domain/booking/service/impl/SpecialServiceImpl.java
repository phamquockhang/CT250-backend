package com.dvk.ct250backend.domain.booking.service.impl;

import com.dvk.ct250backend.app.dto.response.Meta;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.SpecialServiceDTO;
import com.dvk.ct250backend.domain.booking.entity.SpecialService;
import com.dvk.ct250backend.domain.booking.mapper.SpecialServiceMapper;
import com.dvk.ct250backend.domain.booking.repository.SpecialServiceRepository;
import com.dvk.ct250backend.domain.booking.service.SpecialServiceInterface;
import com.dvk.ct250backend.infrastructure.utils.FileUtils;
import com.dvk.ct250backend.infrastructure.utils.RequestParamUtils;
import com.dvk.ct250backend.infrastructure.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SpecialServiceImpl implements SpecialServiceInterface {
    SpecialServiceRepository specialServiceRepository;
    SpecialServiceMapper specialServiceMapper;
    FileUtils fileUtils;
    RequestParamUtils requestParamUtils;
    StringUtils stringUtils;

    @Override
    @Transactional
    public SpecialServiceDTO createSpecialService(SpecialServiceDTO specialServiceDTO, MultipartFile imgUrl) throws IOException {
        File convFile = fileUtils.convertMultipartFileToFile(imgUrl);
        String imageUrl = fileUtils.uploadFileToCloudinary(convFile);
        specialServiceDTO.setImgUrl(imageUrl);
        SpecialService specialService = specialServiceMapper.toSpecialService(specialServiceDTO);

        SpecialService savedSpecialService = specialServiceRepository.save(specialService);
        return specialServiceMapper.toSpecialServiceDTO(savedSpecialService);
    }

    @Override
    @Transactional
    public SpecialServiceDTO updateSpecialService(Integer specialServiceId, SpecialServiceDTO specialServiceDTO, MultipartFile imgUrl) throws IOException, ResourceNotFoundException {
        SpecialService specialService = specialServiceRepository.findById(specialServiceId).orElseThrow(() -> new ResourceNotFoundException("Special service not found"));
        if(imgUrl != null) {
            String currentImageUrl = specialService.getImgUrl();
            if (currentImageUrl != null && !currentImageUrl.isEmpty()) {
                String publicId = fileUtils.getPublicIdFromCloudinary(currentImageUrl);
                fileUtils.deleteFileFromCloudinary(publicId);
            }
            File convFile = fileUtils.convertMultipartFileToFile(imgUrl);
            String imageUrl = fileUtils.uploadFileToCloudinary(convFile);
            specialServiceDTO.setImgUrl(imageUrl);
        }
        specialServiceMapper.updateSpecialServiceFromDTO(specialService, specialServiceDTO);
        specialService=specialServiceRepository.save(specialService);
        return specialServiceMapper.toSpecialServiceDTO(specialService);
    }

    @Override
    public void deleteSpecialService(Integer specialServiceId) throws ResourceNotFoundException {
        SpecialService specialService = specialServiceRepository.findById(specialServiceId).orElseThrow(() -> new ResourceNotFoundException("Special service not found"));
        specialServiceRepository.delete(specialService);
    }


    @Override
    @Cacheable(value = "specialServices")
    public List<SpecialServiceDTO> getAllSpecialServices() {
        return specialServiceRepository.findAll().stream()
                .map(specialServiceMapper::toSpecialServiceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<SpecialServiceDTO> getSpecialServices(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        Specification<SpecialService> spec = getSpecialServiceSpec(params);
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<SpecialService> specialServicesPage = specialServiceRepository.findAll(spec, pageable);
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(specialServicesPage.getTotalPages())
                .total(specialServicesPage.getTotalElements())
                .build();
        return Page.<SpecialServiceDTO>builder()
                .meta(meta)
                .content(specialServicesPage.getContent().stream()
                        .map(specialServiceMapper::toSpecialServiceDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    private Specification<SpecialService> getSpecialServiceSpec(Map<String, String> params) {
        Specification<SpecialService> spec = Specification.where(null);
        if (params.containsKey("query")) {
            String searchValue = stringUtils.normalizeString(params.get("query").trim().toLowerCase());
            String likePattern = "%" + searchValue + "%";
            spec = spec.or((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(
                            criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("serviceName"))),
                            likePattern
                    )
            );
        }

        if (params.containsKey("status")) {
            String status = params.get("status").trim();
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(criteriaBuilder.lower(root.get("status").as(String.class)), status)
            );
        }
        return spec;
    }
}
