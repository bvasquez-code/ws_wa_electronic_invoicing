package com.ccadmin.electronicinvoicing.dto;

import com.ccadmin.electronicinvoicing.entity.EndpointProfile;
import lombok.Data;

@Data
public class IssuerConfig {
    private String issuerRuc;
    private String razonSocial;
    private String usuarioSol;
    private String claveSol;
    private EndpointProfile endpointProfile;
    private String keystorePath;
    private String keystorePassword;
}
