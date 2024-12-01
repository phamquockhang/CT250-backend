package com.dvk.ct250backend.domain.booking.enums;

public enum TicketStatusEnum {
    BOOKED, //	Vé đã được giao cho khách hàng.
    CHECKED_IN,//	Khách hàng đã check-in trực tuyến hoặc tại quầy.
    BOARDED,	//Khách hàng đã lên máy bay.
    NO_SHOW,// Khách hàng không xuất hiện tại chuyến bay.
    REFUNDED, //	Vé đã được hoàn tiền do khách hàng yêu cầu hoặc quy định của hãng.
    RESCHEDULED,//	Vé đã được đổi sang chuyến bay khác.
}
