package com.ccadmin.electronicinvoicing.dto;

import lombok.Data;

@Data
public class TicketStatusDto {
    private String ticket;
    private String status;
    private String code;
    private String message;
}
