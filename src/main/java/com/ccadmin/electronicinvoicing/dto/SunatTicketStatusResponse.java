package com.ccadmin.electronicinvoicing.dto;

import lombok.Data;

@Data
public class SunatTicketStatusResponse {
    private String status;
    private String code;
    private String message;
    private byte[] cdrZip;
}
