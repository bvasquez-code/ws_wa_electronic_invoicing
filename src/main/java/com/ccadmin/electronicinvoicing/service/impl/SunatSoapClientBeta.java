package com.ccadmin.electronicinvoicing.service.impl;

import com.ccadmin.electronicinvoicing.config.ApplicationProperties;
import com.ccadmin.electronicinvoicing.dto.IssuerConfig;
import com.ccadmin.electronicinvoicing.dto.SunatSendBillResponse;
import com.ccadmin.electronicinvoicing.dto.SunatSendTicketResponse;
import com.ccadmin.electronicinvoicing.dto.SunatTicketStatusResponse;
import com.ccadmin.electronicinvoicing.service.SunatSoapClient;
import jakarta.xml.soap.SOAPMessage;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({"dev", "beta"})
public class SunatSoapClientBeta extends AbstractSunatSoapClient implements SunatSoapClient {
    @Autowired
    private ApplicationProperties applicationProperties;

    @Override
    protected String getEndpoint() {
        return applicationProperties.getSunat().getBetaEndpoint();
    }

    @Override
    public SunatSendBillResponse sendBill(String zipFileName, byte[] zipBytes, IssuerConfig config) {
        if (applicationProperties.getSunat().isMockEnabled()) {
            return mockBill(zipFileName, zipBytes);
        }
        try {
            SOAPMessage response = sendSoapMessage(buildSendBillMessage(zipFileName, zipBytes, config));
            SunatSendBillResponse result = new SunatSendBillResponse();
            result.setCode("0");
            result.setMessage("Enviado correctamente");
            result.setCdrZip(Base64.getDecoder().decode(response.getSOAPBody().getTextContent().trim()));
            return result;
        } catch (Exception ex) {
            SunatSendBillResponse result = new SunatSendBillResponse();
            result.setCode("SOAP-01");
            result.setMessage("Error al enviar a SUNAT: " + ex.getMessage());
            return result;
        }
    }

    @Override
    public SunatSendTicketResponse sendSummary(String zipFileName, byte[] zipBytes, IssuerConfig config) {
        if (applicationProperties.getSunat().isMockEnabled()) {
            return mockTicket(zipFileName, zipBytes);
        }
        try {
            SOAPMessage response = sendSoapMessage(buildSendSummaryMessage("sendSummary", zipFileName, zipBytes, config));
            SunatSendTicketResponse result = new SunatSendTicketResponse();
            result.setCode("0");
            result.setMessage("Resumen enviado correctamente");
            result.setTicket(response.getSOAPBody().getTextContent().trim());
            return result;
        } catch (Exception ex) {
            SunatSendTicketResponse result = new SunatSendTicketResponse();
            result.setCode("SOAP-02");
            result.setMessage("Error al enviar resumen: " + ex.getMessage());
            return result;
        }
    }

    @Override
    public SunatSendTicketResponse sendVoided(String zipFileName, byte[] zipBytes, IssuerConfig config) {
        if (applicationProperties.getSunat().isMockEnabled()) {
            return mockTicket(zipFileName, zipBytes);
        }
        try {
            SOAPMessage response = sendSoapMessage(buildSendSummaryMessage("sendSummary", zipFileName, zipBytes, config));
            SunatSendTicketResponse result = new SunatSendTicketResponse();
            result.setCode("0");
            result.setMessage("Baja enviada correctamente");
            result.setTicket(response.getSOAPBody().getTextContent().trim());
            return result;
        } catch (Exception ex) {
            SunatSendTicketResponse result = new SunatSendTicketResponse();
            result.setCode("SOAP-03");
            result.setMessage("Error al enviar baja: " + ex.getMessage());
            return result;
        }
    }

    @Override
    public SunatTicketStatusResponse getStatus(String ticket, IssuerConfig config) {
        if (applicationProperties.getSunat().isMockEnabled()) {
            return mockStatus(ticket);
        }
        try {
            SOAPMessage response = sendSoapMessage(buildGetStatusMessage(ticket, config));
            SunatTicketStatusResponse result = new SunatTicketStatusResponse();
            result.setStatus("PENDING");
            result.setCode("0");
            result.setMessage(response.getSOAPBody().getTextContent().trim());
            return result;
        } catch (Exception ex) {
            SunatTicketStatusResponse result = new SunatTicketStatusResponse();
            result.setStatus("ERROR");
            result.setCode("SOAP-04");
            result.setMessage("Error al consultar ticket: " + ex.getMessage());
            return result;
        }
    }
}
