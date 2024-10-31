package com.dvk.ct250backend.domain.transaction.dto.request;

import lombok.Data;

@Data
public class VNPayCallbackRequest {
    private String vnp_ResponseCode;
    private String vnp_TransactionNo;
    private String vnp_Amount;
    private String vnp_OrderInfo;
    private String vnp_BankCode;
    private String vnp_PayDate;
    private String vnp_TxnRef;
}
