package com.ccadmin.electronicinvoicing.entity;

public enum OutboxAction {
    SEND_BILL,
    SEND_SUMMARY,
    SEND_VOIDED,
    CHECK_TICKET
}
