package com.ccadmin.electronicinvoicing.service.impl;

import com.ccadmin.electronicinvoicing.dto.BaseRegisterDto;
import com.ccadmin.electronicinvoicing.entity.ElectronicDocumentEntity;
import com.ccadmin.electronicinvoicing.util.DateUtil;
import com.ccadmin.electronicinvoicing.util.StringUtil;
import com.ccadmin.electronicinvoicing.service.UblXmlBuilderService;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class UblXmlBuilderServiceImpl implements UblXmlBuilderService {
    @Override
    public String buildInvoiceXml(BaseRegisterDto dto) {
        return buildBasicXml("Invoice", dto);
    }

    @Override
    public String buildBoletaXml(BaseRegisterDto dto) {
        return buildBasicXml("Invoice", dto);
    }

    @Override
    public String buildCreditNoteXml(BaseRegisterDto dto) {
        return buildBasicXml("CreditNote", dto);
    }

    @Override
    public String buildDebitNoteXml(BaseRegisterDto dto) {
        return buildBasicXml("DebitNote", dto);
    }

    @Override
    public String buildSummaryXml(BaseRegisterDto dto) {
        return buildBasicXml("SummaryDocuments", dto);
    }

    @Override
    public String buildVoidedXml(BaseRegisterDto dto) {
        return buildBasicXml("VoidedDocuments", dto);
    }

    private String buildBasicXml(String root, BaseRegisterDto dto) {
        ElectronicDocumentEntity document = dto.document;
        Map<String, Object> extras = dto.extras;
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<").append(root).append(" xmlns=\"urn:oasis:names:specification:ubl:schema:xsd:Invoice-2\"");
        xml.append(" xmlns:cac=\"urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2\"");
        xml.append(" xmlns:cbc=\"urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2\">");
        xml.append("<cbc:ID>").append(document.getSerie()).append("-").append(document.getCorrelativo()).append("</cbc:ID>");
        xml.append("<cbc:IssueDate>").append(DateUtil.formatDate(document.getIssueDate())).append("</cbc:IssueDate>");
        xml.append("<cbc:DocumentCurrencyCode>").append(document.getCurrency()).append("</cbc:DocumentCurrencyCode>");
        xml.append("<cac:AccountingCustomerParty>");
        xml.append("<cbc:CustomerAssignedAccountID>").append(document.getCustomerDocNumber()).append("</cbc:CustomerAssignedAccountID>");
        xml.append("<cbc:AdditionalAccountID>").append(document.getCustomerDocType()).append("</cbc:AdditionalAccountID>");
        xml.append("<cac:Party>");
        xml.append("<cac:PartyName>");
        xml.append("<cbc:Name>").append(escapeXml(document.getCustomerName())).append("</cbc:Name>");
        xml.append("</cac:PartyName>");
        xml.append("</cac:Party>");
        xml.append("</cac:AccountingCustomerParty>");
        xml.append("<cbc:Note>UBL 2.1 generado con placeholders. Completar catálogos con extras.</cbc:Note>");
        if (extras != null && !extras.isEmpty()) {
            xml.append("<cac:AdditionalDocumentReference>");
            xml.append("<cbc:ID>EXTRAS</cbc:ID>");
            xml.append("<cbc:DocumentDescription>")
                .append(escapeXml(extras.toString()))
                .append("</cbc:DocumentDescription>");
            xml.append("</cac:AdditionalDocumentReference>");
        }
        xml.append("</").append(root).append(">\n");
        return xml.toString();
    }

    private String escapeXml(String value) {
        if (StringUtil.isEmpty(value)) {
            return "";
        }
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
