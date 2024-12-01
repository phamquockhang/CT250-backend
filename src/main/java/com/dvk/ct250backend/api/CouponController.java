package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.CouponDTO;
import com.dvk.ct250backend.domain.booking.service.CouponService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CouponController {
    CouponService couponService;

    @GetMapping
    public ApiResponse<Page<CouponDTO>> getCoupon(@RequestParam Map<String, String> params) {
        return ApiResponse.<Page<CouponDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(couponService.getCoupon(params))
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<CouponDTO>> getCouponAll(){
        return ApiResponse.<List<CouponDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(couponService.getAllCoupon())
                .build();
    }

    @GetMapping("/{code}")
    public ApiResponse<CouponDTO> getCouponByCode(@PathVariable String code) throws ResourceNotFoundException {
        return ApiResponse.<CouponDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(couponService.findCouponByCode(code))
                .build();
    }

    @PostMapping
    public ApiResponse<CouponDTO> createCoupon(@RequestBody CouponDTO couponDTO) {
        return ApiResponse.<CouponDTO>builder()
                .status(HttpStatus.CREATED.value())
                .payload(couponService.createCoupon(couponDTO))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CouponDTO> updateCoupon(@PathVariable Integer id, @RequestBody CouponDTO couponDTO) throws ResourceNotFoundException {
        return ApiResponse.<CouponDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(couponService.updateCoupon(id, couponDTO))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCoupon(@PathVariable Integer id) throws ResourceNotFoundException {
        couponService.deleteCoupon(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .build();
    }
}
