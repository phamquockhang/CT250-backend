package com.dvk.ct250backend.domain.booking.enums;

public enum BookingStatusEnum {
    PENDING, // ĐẶT CHỖ ĐANG TRONG QUÁ TRÌNH XỬ LÝ, CHƯA HOÀN TẤT
    CONFIRMED, // ĐẶT CHỖ ĐÃ ĐƯỢC XÁC NHẬN
    CANCELLED, // ĐẶT CHỖ ĐÃ BỊ HỦY
    COMPLETED, // ĐẶT CHỖ ĐÃ HOÀN TẤT
    REFUNDED // ĐẶT CHỖ ĐÃ ĐƯỢC HOÀN TIỀN
}
