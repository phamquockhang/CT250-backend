package com.dvk.ct250backend.domain.booking.service.impl;

import com.dvk.ct250backend.app.dto.request.SearchCriteria;
import com.dvk.ct250backend.app.dto.response.Meta;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.BaggageDTO;
import com.dvk.ct250backend.domain.booking.entity.Baggage;
import com.dvk.ct250backend.domain.booking.mapper.BaggageMapper;
import com.dvk.ct250backend.domain.booking.repository.BaggageRepository;
import com.dvk.ct250backend.domain.booking.service.BaggageService;
import com.dvk.ct250backend.infrastructure.utils.RequestParamUtils;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
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
public class BaggageServiceImpl implements BaggageService {

    BaggageRepository baggageRepository;
    BaggageMapper baggageMapper;
    RequestParamUtils requestParamUtils;

    @Override
    public BaggageDTO createBaggage(BaggageDTO BaggageDTO) {
        Baggage Baggage = baggageMapper.toBaggage(BaggageDTO);
        return baggageMapper.toBaggageDTO(baggageRepository.save(Baggage));
    }

    @Override
    public BaggageDTO updateBaggage(Integer BaggageId, BaggageDTO BaggageDTO) throws ResourceNotFoundException {
        Baggage Baggage = baggageRepository.findById(BaggageId).orElseThrow(() -> new ResourceNotFoundException("Baggage not found"));
        baggageMapper.updateBaggageFromDTO(Baggage, BaggageDTO);
        return baggageMapper.toBaggageDTO(baggageRepository.save(Baggage));
    }

    @Override
    public void deleteBaggage(Integer BaggageId) throws ResourceNotFoundException {
        Baggage Baggage = baggageRepository.findById(BaggageId).orElseThrow(() -> new ResourceNotFoundException("Baggage not found"));
        baggageRepository.delete(Baggage);
    }

    @Override
   @Cacheable(value = "baggage")
    public List<BaggageDTO> getAllBaggage() {
        return baggageRepository.findAll().stream().map(baggageMapper::toBaggageDTO).toList();
    }

    @Override
    public Page<BaggageDTO> getBaggage(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        Specification<Baggage> spec = getBaggageSpec(params);
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<Baggage> BaggagePage = baggageRepository.findAll(spec, pageable);
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(BaggagePage.getTotalPages())
                .total(BaggagePage.getTotalElements())
                .build();
        return Page.<BaggageDTO>builder()
                .meta(meta)
                .content(BaggagePage.getContent().stream()
                        .map(baggageMapper::toBaggageDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    private Specification<Baggage> getBaggageSpec(Map<String, String> params) {
        Specification<Baggage> spec = Specification.where(null);
        List<SearchCriteria> routeTypeCriteria = requestParamUtils.getSearchCriteria(params, "routeType");
        if (!routeTypeCriteria.isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                List<Predicate> predicates = routeTypeCriteria.stream()
                        .map(criteria -> cb.equal(root.get(criteria.getKey()), criteria.getValue()))
                        .toList();
                return cb.or(predicates.toArray(new Predicate[0]));
            });
        }
        return spec;
    }
}