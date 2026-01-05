package com.ccadmin.electronicinvoicing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

@Data
@Entity
@Table(name = "ei_document_line")
public class ElectronicDocumentLineEntity {
    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne
    @JoinColumn(name = "document_id", nullable = false)
    private ElectronicDocumentEntity document;

    @Column(nullable = false, name = "item_number")
    private Integer itemNumber;

    @Column(name = "sku")
    private String sku;

    @Column(nullable = false, name = "description")
    private String description;

    @Column(nullable = false, name = "unit")
    private String unit;

    @Column(nullable = false, precision = 18, scale = 2, name = "quantity")
    private BigDecimal quantity;

    @Column(nullable = false, precision = 18, scale = 2, name = "unit_value")
    private BigDecimal unitValue;

    @Column(nullable = false, precision = 18, scale = 2, name = "unit_price")
    private BigDecimal unitPrice;

    @Column(nullable = false, precision = 18, scale = 2, name = "igv")
    private BigDecimal igv;

    @Column(nullable = false, name = "igv_affectation_type")
    private String igvAffectationType;

    @Column(nullable = false, precision = 18, scale = 2, name = "line_total")
    private BigDecimal lineTotal;

    public ElectronicDocumentLineEntity() {
        this.id = UUID.randomUUID().toString();
    }
}
