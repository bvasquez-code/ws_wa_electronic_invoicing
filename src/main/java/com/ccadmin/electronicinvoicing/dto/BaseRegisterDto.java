package com.ccadmin.electronicinvoicing.dto;

import com.ccadmin.electronicinvoicing.entity.ElectronicDocumentEntity;
import com.ccadmin.electronicinvoicing.entity.ElectronicDocumentLineEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public abstract class BaseRegisterDto {
    @NotNull
    public String issuerRuc;
    @NotNull
    public ElectronicDocumentEntity document;
    public List<ElectronicDocumentLineEntity> lines;
    public Map<String, Object> extras;
    public Boolean generateOnly;
    public Boolean sendToSunat;
    public Boolean async;

    protected BaseRegisterDto() {
        this.lines = new ArrayList<>();
        this.extras = new HashMap<>();
        this.generateOnly = Boolean.FALSE;
        this.sendToSunat = Boolean.TRUE;
        this.async = Boolean.FALSE;
    }
}
