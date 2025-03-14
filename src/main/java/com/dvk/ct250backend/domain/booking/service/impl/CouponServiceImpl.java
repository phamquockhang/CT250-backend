package com.dvk.ct250backend.domain.booking.service.impl;

import com.dvk.ct250backend.app.dto.request.SearchCriteria;
import com.dvk.ct250backend.app.dto.response.Meta;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.CouponDTO;
import com.dvk.ct250backend.domain.booking.entity.Coupon;
import com.dvk.ct250backend.domain.booking.enums.CouponTypeEnum;
import com.dvk.ct250backend.domain.booking.mapper.CouponMapper;
import com.dvk.ct250backend.domain.booking.repository.CouponRepository;
import com.dvk.ct250backend.domain.booking.service.CouponService;
import com.dvk.ct250backend.infrastructure.utils.DateUtils;
import com.dvk.ct250backend.infrastructure.utils.NumberUtils;
import com.dvk.ct250backend.infrastructure.utils.RequestParamUtils;
import com.dvk.ct250backend.infrastructure.utils.StringUtils;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CouponServiceImpl implements CouponService {
    CouponMapper couponMapper;
    CouponRepository couponRepository;
    RequestParamUtils requestParamUtils;
    StringUtils stringUtils;
    DateUtils dateUtils;
    NumberUtils numberUtils;

    @Override
    @Transactional
    public CouponDTO createCoupon(CouponDTO couponDTO) {
        Coupon coupon = couponMapper.toCoupon(couponDTO);
        return couponMapper.toCouponDTO(couponRepository.save(coupon));
    }

    @Override
    public CouponDTO updateCoupon(Integer couponId, CouponDTO couponDTO) throws ResourceNotFoundException {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new ResourceNotFoundException("Coupon not found"));
        couponMapper.updateCouponFromDTO(coupon, couponDTO);
        return couponMapper.toCouponDTO(couponRepository.save(coupon));
    }

    @Override
    public void deleteCoupon(Integer couponId) throws ResourceNotFoundException {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new ResourceNotFoundException("Coupon not found"));
        couponRepository.delete(coupon);
    }

    @Override
    public List<CouponDTO> getAllCoupon() {
        return couponRepository.findAll().stream().map(couponMapper::toCouponDTO).collect(Collectors.toList());
    }

    @Override
    public Page<CouponDTO> getCoupon(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        Specification<Coupon> spec = getCouponSpec(params);
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params, Coupon.class);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<Coupon> couponPage = couponRepository.findAll(spec, pageable);
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(couponPage.getTotalPages())
                .total(couponPage.getTotalElements())
                .build();
        return Page.<CouponDTO>builder()
                .meta(meta)
                .content(couponPage.getContent().stream()
                        .map(couponMapper::toCouponDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    private Specification<Coupon> getCouponSpec(Map<String, String> params) {
        Specification<Coupon> spec = Specification.where(null);
        if (params.containsKey("query")) {
            String searchValue = stringUtils.normalizeString(params.get("query").trim().toLowerCase());
            String likePattern = "%" + searchValue + "%";
            spec = spec.or((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(
                            criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("couponCode"))),
                            likePattern
                    )
            );
        }

        if (params.containsKey("couponType")) {
            List<SearchCriteria> couponTypeCriteria = requestParamUtils.getSearchCriteria(params, "couponType");
            if (!couponTypeCriteria.isEmpty()) {
                spec = spec.and((root, query, cb) -> {
                    List<Predicate> predicates = couponTypeCriteria.stream()
                            .map(criteria -> cb.equal(root.get(criteria.getKey()), criteria.getValue()))
                            .toList();
                    return cb.or(predicates.toArray(new Predicate[0]));
                });
            }
        }
        return spec;
    }

    @Override
    public CouponDTO findCouponByCode(String couponCode) throws ResourceNotFoundException {
        Coupon coupon = couponRepository.findByCouponCode(couponCode)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found for code: " + couponCode));
        return couponMapper.toCouponDTO(coupon);
    }

    @Override
    public boolean isValidCoupon(Coupon coupon) {
        return coupon.getDiscountValue().compareTo(BigDecimal.ZERO) > 0 &&
                dateUtils.isInDateRange(LocalDate.now(), coupon.getValidFrom(), coupon.getValidTo());
    }

    @Override
    public BigDecimal getActualPrice(BigDecimal originalPrice, Coupon coupon) {
        if (coupon != null && isValidCoupon(coupon)) {
            if (coupon.getCouponType().equals(CouponTypeEnum.PERCENTAGE)) {
                BigDecimal discountAmount = originalPrice.multiply(coupon.getDiscountValue().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                return originalPrice.subtract(discountAmount);
            } else {
                return originalPrice.subtract(coupon.getDiscountValue());
            }
        }
        return originalPrice;
    }

    @Override
    @Transactional
    public void decreaseCouponMaxUsage(Coupon coupon) {
        if (coupon.getMaxUsage() > 0) {
            coupon.setMaxUsage(coupon.getMaxUsage() - 1);
            couponRepository.save(coupon);
        }
    }
}
