package com.dvk.ct250backend.domain.flight.service.impl;

import com.dvk.ct250backend.app.dto.response.Meta;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.flight.dto.AirplaneDTO;
import com.dvk.ct250backend.domain.flight.entity.Airplane;
import com.dvk.ct250backend.domain.flight.entity.Airport;
import com.dvk.ct250backend.domain.flight.mapper.AirplaneMapper;
import com.dvk.ct250backend.domain.flight.repository.AirplaneRepository;
import com.dvk.ct250backend.domain.flight.service.AirplaneService;
import com.dvk.ct250backend.infrastructure.utils.RequestParamUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AirplaneServiceImpl implements AirplaneService {

    AirplaneRepository airplaneRepository;
    RequestParamUtils requestParamUtils;
    AirplaneMapper airplaneMapper;

    @Override
    public AirplaneDTO createAirplane(AirplaneDTO airplaneDTO) {
        Airplane airplane = airplaneMapper.toAirplane(airplaneDTO);
        return airplaneMapper.toAirplaneDTO(airplaneRepository.save(airplane));
    }

    @Override
    public void deleteAirplane(Integer id) throws ResourceNotFoundException {
       Airplane airplane = airplaneRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Airplane not found"));
         airplaneRepository.delete(airplane);
    }

    @Override
    public AirplaneDTO updateAirplane(Integer id, AirplaneDTO airplaneDTO) throws ResourceNotFoundException {
        Airplane airplane = airplaneRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Airplane not found"));
        airplaneMapper.updateAirplaneFromDTO(airplane, airplaneDTO);
        return airplaneMapper.toAirplaneDTO(airplaneRepository.save(airplane));
    }

    @Override
    public Page<AirplaneDTO> getAirplanes(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
       Specification<Airplane> spec = getAirplaneSpec(params);
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<Airplane> airplanePage = airplaneRepository.findAll(spec ,pageable);
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(airplanePage.getTotalPages())
                .total(airplanePage.getTotalElements())
                .build();
        return Page.<AirplaneDTO>builder()
                .meta(meta)
                .content(airplanePage.getContent().stream()
                        .map(airplaneMapper::toAirplaneDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    private Specification<Airplane> getAirplaneSpec(Map<String, String> params) {
        Specification<Airplane> spec = Specification.where(null);
        if(params.containsKey("query")){
            String searchValue = params.get("query");
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("modelName")), "%" + searchValue.toLowerCase() + "%")
            ));
        }
        
        return spec;
    }

    @Override
    public List<AirplaneDTO> getAllAirplane() {
       return airplaneRepository.findAll().stream().map(airplaneMapper::toAirplaneDTO).collect(Collectors.toList());
    }
}
