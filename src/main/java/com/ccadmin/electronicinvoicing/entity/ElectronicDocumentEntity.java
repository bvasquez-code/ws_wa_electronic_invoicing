package com.ccadmin.electronicinvoicing.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
@Entity
@Table(name = "ei_document")
public class ElectronicDocumentEntity {
    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne
    @JoinColumn(name = "issuer_id", nullable = false)
    private IssuerEntity issuer;

    @Column(nullable = false, name = "document_type")
    private DocumentType documentType;

    @Column(nullable = false, name = "serie")
    private String serie;

    @Column(nullable = false, name = "correlativo")
    private String correlativo;

    @Column(nullable = false, name = "issue_date")
    private LocalDate issueDate;

    @Column(nullable = false, name = "currency")
    private String currency;

    @Column(nullable = false, name = "customer_doc_type")
    private String customerDocType;

    @Column(nullable = false, name = "customer_doc_number")
    private String customerDocNumber;

    @Column(nullable = false, name = "customer_name")
    private String customerName;

    @Column(nullable = false, precision = 18, scale = 2, name = "op_gravada")
    private BigDecimal opGravada = BigDecimal.ZERO;

    @Column(nullable = false, precision = 18, scale = 2, name = "op_inafecta")
    private BigDecimal opInafecta = BigDecimal.ZERO;

    @Column(nullable = false, precision = 18, scale = 2, name = "op_exonerada")
    private BigDecimal opExonerada = BigDecimal.ZERO;

    @Column(nullable = false, precision = 18, scale = 2, name = "igv")
    private BigDecimal igv = BigDecimal.ZERO;

    @Column(nullable = false, precision = 18, scale = 2, name = "icbper")
    private BigDecimal icbper = BigDecimal.ZERO;

    @Column(nullable = false, precision = 18, scale = 2, name = "total")
    private BigDecimal total = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private DocumentStatus status;

    @Column(name = "sunat_ticket")
    private String sunatTicket;

    @Column(name = "sunat_code")
    private String sunatCode;

    @Column(name = "sunat_message")
    private String sunatMessage;

    @Column(name = "hash_xml")
    private String hashXml;

    @Column(name = "hash_zip")
    private String hashZip;

    @Column(name = "hash_cdr")
    private String hashCdr;

    @Column(nullable = false, name = "created_at")
    private OffsetDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ElectronicDocumentLineEntity> lines = new ArrayList<>();

    public ElectronicDocumentEntity() {
        this.id = UUID.randomUUID().toString();
    }
}
