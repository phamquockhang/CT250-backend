package com.dvk.ct250backend.domain.booking.service;

import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.CouponDTO;
import com.dvk.ct250backend.domain.booking.entity.Coupon;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CouponService {
    CouponDTO createCoupon(CouponDTO couponDTO);
    CouponDTO updateCoupon( Integer couponId ,CouponDTO couponDTO) throws ResourceNotFoundException;
    void deleteCoupon(Integer couponId) throws ResourceNotFoundException;
    List<CouponDTO> getAllCoupon();
    Page<CouponDTO> getCoupon(Map<String, String> params);
    CouponDTO findCouponByCode(String couponCode) throws ResourceNotFoundException;
    boolean isValidCoupon(Coupon coupon);
    BigDecimal getActualPrice(BigDecimal originalPrice, Coupon coupon);
}
