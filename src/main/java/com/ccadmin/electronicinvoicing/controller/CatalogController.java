package com.ccadmin.electronicinvoicing.controller;

import com.ccadmin.electronicinvoicing.dto.ResponseWsDto;
import com.ccadmin.electronicinvoicing.entity.DocumentStatus;
import com.ccadmin.electronicinvoicing.entity.DocumentType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/catalogs")
@Tag(name = "Catálogos", description = "Catálogos mínimos para la API")
public class CatalogController {
    @GetMapping("/document-types")
    @Operation(summary = "Tipos de documento", description = "Lista los códigos de documentos soportados.")
    public ResponseWsDto<List<String>> getDocumentTypes() {
        ResponseWsDto<List<String>> response = new ResponseWsDto<>();
        response.ok = true;
        response.code = "APP-0000";
        response.message = "Catálogo de tipos de documento";
        response.data = Arrays.stream(DocumentType.values())
            .map(DocumentType::getCode)
            .collect(Collectors.toList());
        return response;
    }

    @GetMapping("/status")
    @Operation(summary = "Estados de documento", description = "Lista los estados internos del CPE.")
    public ResponseWsDto<List<String>> getStatuses() {
        ResponseWsDto<List<String>> response = new ResponseWsDto<>();
        response.ok = true;
        response.code = "APP-0000";
        response.message = "Catálogo de estados";
        response.data = Arrays.stream(DocumentStatus.values())
            .map(Enum::name)
            .collect(Collectors.toList());
        return response;
    }
}
