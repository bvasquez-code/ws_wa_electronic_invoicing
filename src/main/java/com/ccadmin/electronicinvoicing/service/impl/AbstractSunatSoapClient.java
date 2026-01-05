package com.ccadmin.electronicinvoicing.service.impl;

import com.ccadmin.electronicinvoicing.config.ApplicationProperties;
import com.ccadmin.electronicinvoicing.dto.IssuerConfig;
import com.ccadmin.electronicinvoicing.dto.SunatSendBillResponse;
import com.ccadmin.electronicinvoicing.dto.SunatSendTicketResponse;
import com.ccadmin.electronicinvoicing.dto.SunatTicketStatusResponse;
import com.ccadmin.electronicinvoicing.util.HashUtil;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.MimeHeaders;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPConnectionFactory;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPMessage;

public abstract class AbstractSunatSoapClient {
    protected ApplicationProperties properties;

    protected abstract String getEndpoint();

    protected SunatSendBillResponse mockBill(String zipFileName, byte[] zipBytes) {
        SunatSendBillResponse response = new SunatSendBillResponse();
        response.setCode("0");
        response.setMessage("Envío simulado OK");
        response.setCdrZip(("CDR-FAKE-" + HashUtil.sha256(zipBytes)).getBytes(StandardCharsets.UTF_8));
        return response;
    }

    protected SunatSendTicketResponse mockTicket(String zipFileName, byte[] zipBytes) {
        SunatSendTicketResponse response = new SunatSendTicketResponse();
        response.setCode("0");
        response.setMessage("Ticket generado en modo mock");
        response.setTicket("TCK" + Math.abs(zipFileName.hashCode()));
        return response;
    }

    protected SunatTicketStatusResponse mockStatus(String ticket) {
        SunatTicketStatusResponse response = new SunatTicketStatusResponse();
        response.setStatus("ACCEPTED");
        response.setCode("0");
        response.setMessage("Ticket procesado en modo mock");
        response.setCdrZip(("CDR-TICKET-" + ticket).getBytes(StandardCharsets.UTF_8));
        return response;
    }

    protected SOAPMessage buildSendBillMessage(String zipFileName, byte[] zipBytes, IssuerConfig config) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage message = messageFactory.createMessage();
        SOAPBody body = message.getSOAPBody();
        SOAPElement sendBill = body.addChildElement("sendBill", "ser", "http://service.sunat.gob.pe");
        sendBill.addChildElement("fileName").setTextContent(zipFileName);
        sendBill.addChildElement("contentFile").setTextContent(Base64.getEncoder().encodeToString(zipBytes));
        message.getMimeHeaders().addHeader("SOAPAction", "sendBill");
        applySecurityHeaders(message.getMimeHeaders(), config);
        message.saveChanges();
        return message;
    }

    protected SOAPMessage buildSendSummaryMessage(String action, String zipFileName, byte[] zipBytes, IssuerConfig config) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage message = messageFactory.createMessage();
        SOAPBody body = message.getSOAPBody();
        SOAPElement sendSummary = body.addChildElement(action, "ser", "http://service.sunat.gob.pe");
        sendSummary.addChildElement("fileName").setTextContent(zipFileName);
        sendSummary.addChildElement("contentFile").setTextContent(Base64.getEncoder().encodeToString(zipBytes));
        message.getMimeHeaders().addHeader("SOAPAction", action);
        applySecurityHeaders(message.getMimeHeaders(), config);
        message.saveChanges();
        return message;
    }

    protected SOAPMessage buildGetStatusMessage(String ticket, IssuerConfig config) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage message = messageFactory.createMessage();
        SOAPBody body = message.getSOAPBody();
        SOAPElement getStatus = body.addChildElement("getStatus", "ser", "http://service.sunat.gob.pe");
        getStatus.addChildElement("ticket").setTextContent(ticket);
        message.getMimeHeaders().addHeader("SOAPAction", "getStatus");
        applySecurityHeaders(message.getMimeHeaders(), config);
        message.saveChanges();
        return message;
    }

    protected SOAPMessage sendSoapMessage(SOAPMessage message) throws Exception {
        SOAPConnectionFactory connectionFactory = SOAPConnectionFactory.newInstance();
        try (SOAPConnection connection = connectionFactory.createConnection()) {
            return connection.call(message, getEndpoint());
        }
    }

    protected void applySecurityHeaders(MimeHeaders headers, IssuerConfig config) {
        String username = config.getIssuerRuc() + config.getUsuarioSol();
        headers.addHeader("Username", username);
        headers.addHeader("Password", config.getClaveSol());
    }
}
