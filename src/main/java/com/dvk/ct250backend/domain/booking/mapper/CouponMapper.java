package com.dvk.ct250backend.domain.booking.mapper;

import com.dvk.ct250backend.domain.booking.dto.CouponDTO;
import com.dvk.ct250backend.domain.booking.entity.Coupon;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = CouponMapper.class)
public interface CouponMapper {
    Coupon toCoupon(CouponDTO couponDTO);
    CouponDTO toCouponDTO(Coupon coupon);
    void updateCouponFromDTO(@MappingTarget Coupon coupon, CouponDTO couponDTO);
}
