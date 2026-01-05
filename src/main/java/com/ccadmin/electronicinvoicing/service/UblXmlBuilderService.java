package com.ccadmin.electronicinvoicing.service;

import com.ccadmin.electronicinvoicing.dto.BaseRegisterDto;

public interface UblXmlBuilderService {
    String buildInvoiceXml(BaseRegisterDto dto);

    String buildBoletaXml(BaseRegisterDto dto);

    String buildCreditNoteXml(BaseRegisterDto dto);

    String buildDebitNoteXml(BaseRegisterDto dto);

    String buildSummaryXml(BaseRegisterDto dto);

    String buildVoidedXml(BaseRegisterDto dto);
}
