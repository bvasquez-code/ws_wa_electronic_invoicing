package com.ccadmin.electronicinvoicing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;

@Data
@Entity
@Table(name = "ei_outbox")
public class OutboxEntity {
    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, name = "document_id")
    private String documentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "action")
    private OutboxAction action;

    @Column(nullable = false, name = "attempts")
    private Integer attempts = 0;

    @Column(nullable = false, name = "next_retry_at")
    private OffsetDateTime nextRetryAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private OutboxStatus status;

    @Column(name = "last_error")
    private String lastError;

    @Column(nullable = false, name = "created_at")
    private OffsetDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    private OffsetDateTime updatedAt;

    public OutboxEntity() {
        this.id = UUID.randomUUID().toString();
    }
}
