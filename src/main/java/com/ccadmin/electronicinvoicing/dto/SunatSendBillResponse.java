package com.ccadmin.electronicinvoicing.dto;

import lombok.Data;

@Data
public class SunatSendBillResponse {
    private String code;
    private String message;
    private byte[] cdrZip;
}
