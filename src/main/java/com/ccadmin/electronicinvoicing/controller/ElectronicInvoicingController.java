package com.ccadmin.electronicinvoicing.controller;

import com.ccadmin.electronicinvoicing.dto.BoletaRegisterDto;
import com.ccadmin.electronicinvoicing.dto.CreditNoteRegisterDto;
import com.ccadmin.electronicinvoicing.dto.DebitNoteRegisterDto;
import com.ccadmin.electronicinvoicing.dto.InvoiceRegisterDto;
import com.ccadmin.electronicinvoicing.dto.ResponseWsDto;
import com.ccadmin.electronicinvoicing.dto.SummaryRegisterDto;
import com.ccadmin.electronicinvoicing.dto.TicketStatusDto;
import com.ccadmin.electronicinvoicing.dto.VoidedRegisterDto;
import com.ccadmin.electronicinvoicing.entity.ElectronicDocumentEntity;
import com.ccadmin.electronicinvoicing.service.DocumentFileService;
import com.ccadmin.electronicinvoicing.service.ElectronicInvoicingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cpe")
@Tag(name = "CPE", description = "Operaciones de emisión, resumen y consulta")
public class ElectronicInvoicingController {
    @Autowired
    private ElectronicInvoicingService electronicInvoicingService;

    @Autowired
    private DocumentFileService documentFileService;

    @PostMapping("/invoices")
    @Operation(summary = "Emitir Factura (01)", description = "Genera XML, firma, ZIP y envía a SUNAT.")
    @ApiResponse(responseCode = "200", description = "Respuesta estándar",
        content = @Content(examples = @ExampleObject(value = "{ \"ok\": true, \"code\": \"APP-0000\", \"message\": \"Documento procesado correctamente\" }")))
    public ResponseWsDto<ElectronicDocumentEntity> issueInvoice(@Valid @RequestBody InvoiceRegisterDto dto) {
        try {
            return electronicInvoicingService.issueInvoice(dto);
        } catch (Exception ex) {
            return buildError("APP-1001", "Error al emitir factura", ex);
        }
    }

    @PostMapping("/boletas")
    @Operation(summary = "Emitir Boleta (03)", description = "Genera XML, firma, ZIP y envía a SUNAT.")
    @ApiResponse(responseCode = "200", description = "Respuesta estándar",
        content = @Content(examples = @ExampleObject(value = "{ \"ok\": true, \"code\": \"APP-0000\", \"message\": \"Documento procesado correctamente\" }")))
    public ResponseWsDto<ElectronicDocumentEntity> issueBoleta(@Valid @RequestBody BoletaRegisterDto dto) {
        try {
            return electronicInvoicingService.issueBoleta(dto);
        } catch (Exception ex) {
            return buildError("APP-1002", "Error al emitir boleta", ex);
        }
    }

    @PostMapping("/credit-notes")
    @Operation(summary = "Emitir Nota de Crédito (07)", description = "Genera XML, firma, ZIP y envía a SUNAT.")
    @ApiResponse(responseCode = "200", description = "Respuesta estándar",
        content = @Content(examples = @ExampleObject(value = "{ \"ok\": true, \"code\": \"APP-0000\", \"message\": \"Documento procesado correctamente\" }")))
    public ResponseWsDto<ElectronicDocumentEntity> issueCreditNote(@Valid @RequestBody CreditNoteRegisterDto dto) {
        try {
            return electronicInvoicingService.issueCreditNote(dto);
        } catch (Exception ex) {
            return buildError("APP-1003", "Error al emitir nota de crédito", ex);
        }
    }

    @PostMapping("/debit-notes")
    @Operation(summary = "Emitir Nota de Débito (08)", description = "Genera XML, firma, ZIP y envía a SUNAT.")
    @ApiResponse(responseCode = "200", description = "Respuesta estándar",
        content = @Content(examples = @ExampleObject(value = "{ \"ok\": true, \"code\": \"APP-0000\", \"message\": \"Documento procesado correctamente\" }")))
    public ResponseWsDto<ElectronicDocumentEntity> issueDebitNote(@Valid @RequestBody DebitNoteRegisterDto dto) {
        try {
            return electronicInvoicingService.issueDebitNote(dto);
        } catch (Exception ex) {
            return buildError("APP-1004", "Error al emitir nota de débito", ex);
        }
    }

    @PostMapping("/summaries")
    @Operation(summary = "Generar Resumen Diario", description = "Genera resumen de boletas y devuelve ticket.")
    @ApiResponse(responseCode = "200", description = "Respuesta estándar",
        content = @Content(examples = @ExampleObject(value = "{ \"ok\": true, \"code\": \"APP-0000\", \"message\": \"Documento procesado correctamente\" }")))
    public ResponseWsDto<ElectronicDocumentEntity> createSummary(@Valid @RequestBody SummaryRegisterDto dto) {
        try {
            return electronicInvoicingService.createSummary(dto);
        } catch (Exception ex) {
            return buildError("APP-1005", "Error al generar resumen", ex);
        }
    }

    @PostMapping("/voided")
    @Operation(summary = "Generar Comunicación de Baja", description = "Genera baja y devuelve ticket.")
    @ApiResponse(responseCode = "200", description = "Respuesta estándar",
        content = @Content(examples = @ExampleObject(value = "{ \"ok\": true, \"code\": \"APP-0000\", \"message\": \"Documento procesado correctamente\" }")))
    public ResponseWsDto<ElectronicDocumentEntity> createVoided(@Valid @RequestBody VoidedRegisterDto dto) {
        try {
            return electronicInvoicingService.createVoided(dto);
        } catch (Exception ex) {
            return buildError("APP-1006", "Error al generar comunicación de baja", ex);
        }
    }

    @GetMapping("/{documentId}")
    @Operation(summary = "Consultar documento", description = "Obtiene detalle y estado.")
    public ResponseWsDto<ElectronicDocumentEntity> findById(@PathVariable String documentId) {
        try {
            return electronicInvoicingService.findById(documentId);
        } catch (Exception ex) {
            return buildError("APP-1007", "Error al consultar documento", ex);
        }
    }

    @GetMapping("/{documentId}/xml")
    @Operation(summary = "Descargar XML firmado", description = "Devuelve bytes del XML firmado.")
    public ResponseWsDto<byte[]> downloadXml(@PathVariable String documentId) {
        try {
            return documentFileService.downloadXml(documentId);
        } catch (Exception ex) {
            return buildError("APP-1008", "Error al descargar XML", ex);
        }
    }

    @GetMapping("/{documentId}/zip")
    @Operation(summary = "Descargar ZIP enviado", description = "Devuelve bytes del ZIP enviado a SUNAT.")
    public ResponseWsDto<byte[]> downloadZip(@PathVariable String documentId) {
        try {
            return documentFileService.downloadZip(documentId);
        } catch (Exception ex) {
            return buildError("APP-1009", "Error al descargar ZIP", ex);
        }
    }

    @GetMapping("/{documentId}/cdr")
    @Operation(summary = "Descargar CDR", description = "Devuelve bytes del CDR si existe.")
    public ResponseWsDto<byte[]> downloadCdr(@PathVariable String documentId) {
        try {
            return documentFileService.downloadCdr(documentId);
        } catch (Exception ex) {
            return buildError("APP-1010", "Error al descargar CDR", ex);
        }
    }

    @GetMapping("/tickets/{ticket}/status")
    @Operation(summary = "Consultar ticket", description = "Consulta el estado del ticket en SUNAT.")
    public ResponseWsDto<TicketStatusDto> checkTicket(@PathVariable String ticket) {
        try {
            return electronicInvoicingService.checkTicket(ticket);
        } catch (Exception ex) {
            return buildError("APP-1011", "Error al consultar ticket", ex);
        }
    }

    private <T> ResponseWsDto<T> buildError(String code, String message, Exception ex) {
        ResponseWsDto<T> response = new ResponseWsDto<>();
        response.ok = false;
        response.code = code;
        response.message = message;
        response.errors = Collections.singletonList(ex.getMessage());
        return response;
    }
}
