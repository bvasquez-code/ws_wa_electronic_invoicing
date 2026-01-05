package com.ccadmin.electronicinvoicing.dto;

import lombok.Data;

@Data
public class SunatSendTicketResponse {
    private String code;
    private String message;
    private String ticket;
}
