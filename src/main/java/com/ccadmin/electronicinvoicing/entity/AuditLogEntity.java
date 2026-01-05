package com.ccadmin.electronicinvoicing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;

@Data
@Entity
@Table(name = "ei_audit_log")
public class AuditLogEntity {
    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne
    @JoinColumn(name = "document_id")
    private ElectronicDocumentEntity document;

    @Column(nullable = false, name = "event")
    private String event;

    @Column(nullable = false, length = 1000, name = "payload_min")
    private String payloadMin;

    @Column(nullable = false, name = "created_at")
    private OffsetDateTime createdAt;

    public AuditLogEntity() {
        this.id = UUID.randomUUID().toString();
    }
}
