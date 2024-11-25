package com.dvk.ct250backend.domain.booking.enums;

public enum BookingStatusEnum {
    INIT, // ĐẶT CHỖ ĐANG TRONG QUÁ TRÌNH XỬ LÝ, CHƯA HOÀN TẤT
    PENDING, // ĐẶT CHỖ ĐANG TRONG QUÁ TRÌNH XỬ LÝ, CHƯA HOÀN TẤT
    CANCELLED, // ĐẶT CHỖ ĐÃ BỊ HỦY
    RESERVED, // ĐẶT CHỖ ĐANG TRONG QUÁ TRÌNH XỬ LÝ, CHƯA HOÀN TẤT
    PAID, // ĐẶT CHỖ ĐÃ ĐƯỢC THANH TOÁN
}
