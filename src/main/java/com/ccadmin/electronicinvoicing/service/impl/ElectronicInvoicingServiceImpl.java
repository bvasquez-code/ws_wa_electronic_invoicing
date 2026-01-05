package com.ccadmin.electronicinvoicing.service.impl;

import com.ccadmin.electronicinvoicing.config.ApplicationProperties;
import com.ccadmin.electronicinvoicing.dto.BaseRegisterDto;
import com.ccadmin.electronicinvoicing.dto.BoletaRegisterDto;
import com.ccadmin.electronicinvoicing.dto.CreditNoteRegisterDto;
import com.ccadmin.electronicinvoicing.dto.DebitNoteRegisterDto;
import com.ccadmin.electronicinvoicing.dto.InvoiceRegisterDto;
import com.ccadmin.electronicinvoicing.dto.IssuerConfig;
import com.ccadmin.electronicinvoicing.dto.ResponseWsDto;
import com.ccadmin.electronicinvoicing.dto.SummaryRegisterDto;
import com.ccadmin.electronicinvoicing.dto.SunatSendBillResponse;
import com.ccadmin.electronicinvoicing.dto.SunatSendTicketResponse;
import com.ccadmin.electronicinvoicing.dto.SunatTicketStatusResponse;
import com.ccadmin.electronicinvoicing.dto.TicketStatusDto;
import com.ccadmin.electronicinvoicing.dto.VoidedRegisterDto;
import com.ccadmin.electronicinvoicing.entity.AuditLogEntity;
import com.ccadmin.electronicinvoicing.entity.DocumentStatus;
import com.ccadmin.electronicinvoicing.entity.DocumentType;
import com.ccadmin.electronicinvoicing.entity.ElectronicDocumentEntity;
import com.ccadmin.electronicinvoicing.entity.ElectronicDocumentLineEntity;
import com.ccadmin.electronicinvoicing.entity.IssuerEntity;
import com.ccadmin.electronicinvoicing.entity.OutboxAction;
import com.ccadmin.electronicinvoicing.entity.OutboxEntity;
import com.ccadmin.electronicinvoicing.entity.OutboxStatus;
import com.ccadmin.electronicinvoicing.exception.ApiException;
import com.ccadmin.electronicinvoicing.repository.AuditLogRepository;
import com.ccadmin.electronicinvoicing.repository.ElectronicDocumentRepository;
import com.ccadmin.electronicinvoicing.repository.IssuerRepository;
import com.ccadmin.electronicinvoicing.repository.OutboxRepository;
import com.ccadmin.electronicinvoicing.service.ElectronicInvoicingService;
import com.ccadmin.electronicinvoicing.service.FileStoreService;
import com.ccadmin.electronicinvoicing.service.SunatSoapClient;
import com.ccadmin.electronicinvoicing.service.UblXmlBuilderService;
import com.ccadmin.electronicinvoicing.service.XmlSignerService;
import com.ccadmin.electronicinvoicing.service.ZipService;
import com.ccadmin.electronicinvoicing.util.HashUtil;
import com.ccadmin.electronicinvoicing.util.ValidationUtil;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ElectronicInvoicingServiceImpl implements ElectronicInvoicingService {
    @Autowired
    private IssuerRepository issuerRepository;

    @Autowired
    private ElectronicDocumentRepository documentRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private UblXmlBuilderService xmlBuilderService;

    @Autowired
    private XmlSignerService xmlSignerService;

    @Autowired
    private ZipService zipService;

    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private SunatSoapClient sunatSoapClient;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Override
    @Transactional
    public ResponseWsDto<ElectronicDocumentEntity> issueInvoice(InvoiceRegisterDto dto) {
        return issueDocument(dto, DocumentType.INVOICE);
    }

    @Override
    @Transactional
    public ResponseWsDto<ElectronicDocumentEntity> issueBoleta(BoletaRegisterDto dto) {
        return issueDocument(dto, DocumentType.BOLETA);
    }

    @Override
    @Transactional
    public ResponseWsDto<ElectronicDocumentEntity> issueCreditNote(CreditNoteRegisterDto dto) {
        return issueDocument(dto, DocumentType.CREDIT_NOTE);
    }

    @Override
    @Transactional
    public ResponseWsDto<ElectronicDocumentEntity> issueDebitNote(DebitNoteRegisterDto dto) {
        return issueDocument(dto, DocumentType.DEBIT_NOTE);
    }

    @Override
    @Transactional
    public ResponseWsDto<ElectronicDocumentEntity> createSummary(SummaryRegisterDto dto) {
        return issueDocument(dto, DocumentType.SUMMARY);
    }

    @Override
    @Transactional
    public ResponseWsDto<ElectronicDocumentEntity> createVoided(VoidedRegisterDto dto) {
        return issueDocument(dto, DocumentType.VOIDED);
    }

    @Override
    public ResponseWsDto<ElectronicDocumentEntity> findById(String documentId) {
        ElectronicDocumentEntity document = documentRepository.findById(documentId)
            .orElseThrow(() -> new ApiException("APP-0004", "Documento no encontrado"));
        ResponseWsDto<ElectronicDocumentEntity> response = new ResponseWsDto<>();
        response.ok = true;
        response.code = "APP-0000";
        response.message = "Consulta exitosa";
        response.data = document;
        return response;
    }

    @Override
    public ResponseWsDto<TicketStatusDto> checkTicket(String ticket) {
        List<ElectronicDocumentEntity> documents = documentRepository.findBySunatTicket(ticket);
        if (documents.isEmpty()) {
            throw new ApiException("APP-0005", "Ticket no encontrado");
        }
        ElectronicDocumentEntity document = documents.get(0);
        IssuerConfig config = buildIssuerConfig(document.getIssuer());
        SunatTicketStatusResponse statusResponse = sunatSoapClient.getStatus(ticket, config);
        if ("ACCEPTED".equalsIgnoreCase(statusResponse.getStatus())) {
            document.setStatus(DocumentStatus.ACCEPTED);
        }
        document.setSunatCode(statusResponse.getCode());
        document.setSunatMessage(statusResponse.getMessage());
        document.setUpdatedAt(OffsetDateTime.now());
        if (statusResponse.getCdrZip() != null) {
            fileStoreService.storeCdrZip(document, statusResponse.getCdrZip(), document.getId() + "-cdr.zip");
            document.setHashCdr(HashUtil.sha256(statusResponse.getCdrZip()));
        }
        documentRepository.save(document);

        TicketStatusDto dto = new TicketStatusDto();
        dto.setTicket(ticket);
        dto.setStatus(statusResponse.getStatus());
        dto.setCode(statusResponse.getCode());
        dto.setMessage(statusResponse.getMessage());

        ResponseWsDto<TicketStatusDto> response = new ResponseWsDto<>();
        response.ok = true;
        response.code = "APP-0000";
        response.message = "Ticket consultado";
        response.data = dto;
        return response;
    }

    @Transactional
    public void processOutbox(OutboxEntity outbox) {
        ElectronicDocumentEntity document = documentRepository.findById(outbox.getDocumentId())
            .orElseThrow(() -> new ApiException("APP-0004", "Documento no encontrado"));
        DocumentType type = document.getDocumentType();
        processDocument(document, buildRegisterFromDocument(document), type, document.getIssuer());
    }

    private ResponseWsDto<ElectronicDocumentEntity> issueDocument(BaseRegisterDto dto, DocumentType type) {
        validate(dto, type);
        IssuerEntity issuer = issuerRepository.findByRuc(dto.issuerRuc)
            .orElseThrow(() -> new ApiException("APP-0003", "Emisor no encontrado"));

        ElectronicDocumentEntity document = dto.document;
        document.setIssuer(issuer);
        document.setDocumentType(type);
        document.setStatus(DocumentStatus.DRAFT);
        if (document.getIssueDate() == null) {
            document.setIssueDate(java.time.LocalDate.now());
        }
        OffsetDateTime now = OffsetDateTime.now();
        document.setCreatedAt(now);
        document.setUpdatedAt(now);

        attachLines(document, dto.lines);
        document = documentRepository.save(document);

        registerAudit(document, "DRAFT", "Documento creado en estado DRAFT");

        if (Boolean.TRUE.equals(dto.async)) {
            enqueue(document, type);
            ResponseWsDto<ElectronicDocumentEntity> response = new ResponseWsDto<>();
            response.ok = true;
            response.code = "APP-0000";
            response.message = "Documento encolado para procesamiento";
            response.data = document;
            return response;
        }

        processDocument(document, dto, type, issuer);
        ResponseWsDto<ElectronicDocumentEntity> response = new ResponseWsDto<>();
        response.ok = true;
        response.code = "APP-0000";
        response.message = "Documento procesado correctamente";
        response.data = document;
        return response;
    }

    private void processDocument(ElectronicDocumentEntity document, BaseRegisterDto dto, DocumentType type, IssuerEntity issuer) {
        String unsignedXml = buildXml(dto, type);
        String signedXml = xmlSignerService.signXml(unsignedXml, buildIssuerConfig(issuer));
        byte[] signedBytes = signedXml.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        fileStoreService.storeSignedXml(document, signedBytes, document.getId() + ".xml");
        document.setStatus(DocumentStatus.SIGNED);
        document.setHashXml(HashUtil.sha256(signedBytes));

        byte[] zipBytes = zipService.zipSingle(document.getId() + ".xml", signedBytes, document.getId() + ".zip");
        fileStoreService.storeZip(document, zipBytes, document.getId() + ".zip");
        document.setStatus(DocumentStatus.ZIPPED);
        document.setHashZip(HashUtil.sha256(zipBytes));

        if (Boolean.FALSE.equals(dto.sendToSunat) || Boolean.TRUE.equals(dto.generateOnly)) {
            documentRepository.save(document);
            registerAudit(document, "ZIP", "XML firmado y ZIP generado sin envío");
            return;
        }

        if (type == DocumentType.SUMMARY) {
            SunatSendTicketResponse response = sunatSoapClient.sendSummary(document.getId() + ".zip", zipBytes, buildIssuerConfig(issuer));
            document.setSunatTicket(response.getTicket());
            document.setSunatCode(response.getCode());
            document.setSunatMessage(response.getMessage());
            document.setStatus(DocumentStatus.PENDING_TICKET);
            registerAudit(document, "SUMMARY", response.getMessage());
        } else if (type == DocumentType.VOIDED) {
            SunatSendTicketResponse response = sunatSoapClient.sendVoided(document.getId() + ".zip", zipBytes, buildIssuerConfig(issuer));
            document.setSunatTicket(response.getTicket());
            document.setSunatCode(response.getCode());
            document.setSunatMessage(response.getMessage());
            document.setStatus(DocumentStatus.PENDING_TICKET);
            registerAudit(document, "VOIDED", response.getMessage());
        } else {
            SunatSendBillResponse response = sunatSoapClient.sendBill(document.getId() + ".zip", zipBytes, buildIssuerConfig(issuer));
            document.setSunatCode(response.getCode());
            document.setSunatMessage(response.getMessage());
            if (response.getCdrZip() != null) {
                fileStoreService.storeCdrZip(document, response.getCdrZip(), document.getId() + "-cdr.zip");
                document.setHashCdr(HashUtil.sha256(response.getCdrZip()));
            }
            document.setStatus("0".equals(response.getCode()) ? DocumentStatus.ACCEPTED : DocumentStatus.REJECTED);
            registerAudit(document, "SEND_BILL", response.getMessage());
        }
        document.setUpdatedAt(OffsetDateTime.now());
        documentRepository.save(document);
    }

    private void enqueue(ElectronicDocumentEntity document, DocumentType type) {
        OutboxEntity outbox = new OutboxEntity();
        outbox.setDocumentId(document.getId());
        outbox.setAction(type == DocumentType.SUMMARY ? OutboxAction.SEND_SUMMARY :
            type == DocumentType.VOIDED ? OutboxAction.SEND_VOIDED : OutboxAction.SEND_BILL);
        outbox.setStatus(OutboxStatus.PENDING);
        outbox.setAttempts(0);
        outbox.setNextRetryAt(OffsetDateTime.now());
        outbox.setCreatedAt(OffsetDateTime.now());
        outbox.setUpdatedAt(OffsetDateTime.now());
        outboxRepository.save(outbox);
    }

    private void validate(BaseRegisterDto dto, DocumentType type) {
        if (dto == null || dto.document == null) {
            throw new ApiException("APP-0001", "El documento es requerido");
        }
        if (!ValidationUtil.isValidRuc(dto.issuerRuc)) {
            throw new ApiException("APP-0001", "RUC de emisor inválido");
        }
        if (!ValidationUtil.isValidSerie(dto.document.getSerie()) || !ValidationUtil.isValidCorrelativo(dto.document.getCorrelativo())) {
            throw new ApiException("APP-0001", "Serie o correlativo inválidos");
        }
        if (dto.document.getTotal() == null || dto.document.getTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new ApiException("APP-0001", "El total debe ser mayor o igual a cero");
        }
        if (dto.document.getDocumentType() == null) {
            dto.document.setDocumentType(type);
        }
    }

    private void attachLines(ElectronicDocumentEntity document, List<ElectronicDocumentLineEntity> lines) {
        if (lines == null || lines.isEmpty()) {
            return;
        }
        lines.forEach(line -> line.setDocument(document));
        document.setLines(lines);
    }

    private String buildXml(BaseRegisterDto dto, DocumentType type) {
        return switch (type) {
            case INVOICE -> xmlBuilderService.buildInvoiceXml(dto);
            case BOLETA -> xmlBuilderService.buildBoletaXml(dto);
            case CREDIT_NOTE -> xmlBuilderService.buildCreditNoteXml(dto);
            case DEBIT_NOTE -> xmlBuilderService.buildDebitNoteXml(dto);
            case SUMMARY -> xmlBuilderService.buildSummaryXml(dto);
            case VOIDED -> xmlBuilderService.buildVoidedXml(dto);
        };
    }

    private IssuerConfig buildIssuerConfig(IssuerEntity issuer) {
        IssuerConfig config = new IssuerConfig();
        config.setIssuerRuc(issuer.getRuc());
        config.setRazonSocial(issuer.getRazonSocial());
        config.setUsuarioSol(issuer.getUsuarioSol());
        config.setClaveSol(issuer.getClaveSol());
        config.setEndpointProfile(issuer.getEndpointProfile());
        config.setKeystorePath(applicationProperties.getKeystore().getPath());
        config.setKeystorePassword(applicationProperties.getKeystore().getPassword());
        return config;
    }

    private void registerAudit(ElectronicDocumentEntity document, String event, String payload) {
        AuditLogEntity log = new AuditLogEntity();
        log.setDocument(document);
        log.setEvent(event);
        log.setPayloadMin(payload);
        log.setCreatedAt(OffsetDateTime.now());
        auditLogRepository.save(log);
    }

    private BaseRegisterDto buildRegisterFromDocument(ElectronicDocumentEntity document) {
        BaseRegisterDto dto = new BaseRegisterDto() {
        };
        dto.issuerRuc = document.getIssuer().getRuc();
        dto.document = document;
        dto.lines = document.getLines();
        dto.generateOnly = Boolean.FALSE;
        dto.sendToSunat = Boolean.TRUE;
        dto.async = Boolean.FALSE;
        return dto;
    }
}
