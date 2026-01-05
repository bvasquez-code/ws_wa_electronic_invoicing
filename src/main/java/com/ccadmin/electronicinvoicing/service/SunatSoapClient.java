package com.ccadmin.electronicinvoicing.service;

import com.ccadmin.electronicinvoicing.dto.IssuerConfig;
import com.ccadmin.electronicinvoicing.dto.SunatSendBillResponse;
import com.ccadmin.electronicinvoicing.dto.SunatSendTicketResponse;
import com.ccadmin.electronicinvoicing.dto.SunatTicketStatusResponse;

public interface SunatSoapClient {
    SunatSendBillResponse sendBill(String zipFileName, byte[] zipBytes, IssuerConfig config);

    SunatSendTicketResponse sendSummary(String zipFileName, byte[] zipBytes, IssuerConfig config);

    SunatSendTicketResponse sendVoided(String zipFileName, byte[] zipBytes, IssuerConfig config);

    SunatTicketStatusResponse getStatus(String ticket, IssuerConfig config);
}
