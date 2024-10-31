package com.dvk.ct250backend.domain.booking.enums;

public enum BookingStatusEnum {
    INIT, // ĐẶT CHỖ ĐANG TRONG QUÁ TRÌNH XỬ LÝ, CHƯA HOÀN TẤT
    PENDING, // ĐẶT CHỖ ĐANG TRONG QUÁ TRÌNH XỬ LÝ, CHƯA HOÀN TẤT
    CONFIRMED, // ĐẶT CHỖ ĐÃ ĐƯỢC XÁC NHẬN
    CANCELLED, // ĐẶT CHỖ ĐÃ BỊ HỦY
    COMPLETED, // ĐẶT CHỖ ĐÃ HOÀN TẤT
    REFUNDED, // ĐẶT CHỖ ĐÃ ĐƯỢC HOÀN TIỀN
    RESERVED, // ĐẶT CHỖ ĐANG TRONG QUÁ TRÌNH XỬ LÝ, CHƯA HOÀN TẤT
    PAID, // ĐẶT CHỖ ĐÃ ĐƯỢC THANH TOÁN
}
