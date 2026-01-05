package com.ccadmin.electronicinvoicing.service;

import com.ccadmin.electronicinvoicing.dto.BoletaRegisterDto;
import com.ccadmin.electronicinvoicing.dto.CreditNoteRegisterDto;
import com.ccadmin.electronicinvoicing.dto.DebitNoteRegisterDto;
import com.ccadmin.electronicinvoicing.dto.InvoiceRegisterDto;
import com.ccadmin.electronicinvoicing.dto.ResponseWsDto;
import com.ccadmin.electronicinvoicing.dto.SummaryRegisterDto;
import com.ccadmin.electronicinvoicing.dto.TicketStatusDto;
import com.ccadmin.electronicinvoicing.dto.VoidedRegisterDto;
import com.ccadmin.electronicinvoicing.entity.ElectronicDocumentEntity;

public interface ElectronicInvoicingService {
    ResponseWsDto<ElectronicDocumentEntity> issueInvoice(InvoiceRegisterDto dto);

    ResponseWsDto<ElectronicDocumentEntity> issueBoleta(BoletaRegisterDto dto);

    ResponseWsDto<ElectronicDocumentEntity> issueCreditNote(CreditNoteRegisterDto dto);

    ResponseWsDto<ElectronicDocumentEntity> issueDebitNote(DebitNoteRegisterDto dto);

    ResponseWsDto<ElectronicDocumentEntity> createSummary(SummaryRegisterDto dto);

    ResponseWsDto<ElectronicDocumentEntity> createVoided(VoidedRegisterDto dto);

    ResponseWsDto<ElectronicDocumentEntity> findById(String documentId);

    ResponseWsDto<TicketStatusDto> checkTicket(String ticket);
}
