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
@Table(name = "ei_issuer")
public class IssuerEntity {
    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, unique = true, length = 11, name = "ruc")
    private String ruc;

    @Column(nullable = false, name = "razon_social")
    private String razonSocial;

    @Column(nullable = false, name = "ubigeo")
    private String ubigeo;

    @Column(nullable = false, name = "direccion")
    private String direccion;

    @Column(name = "usuario_sol")
    private String usuarioSol;

    @Column(name = "clave_sol")
    private String claveSol;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "endpoint_profile")
    private EndpointProfile endpointProfile;

    @Column(nullable = false, name = "created_at")
    private OffsetDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    private OffsetDateTime updatedAt;

    public IssuerEntity() {
        this.id = UUID.randomUUID().toString();
    }
}
