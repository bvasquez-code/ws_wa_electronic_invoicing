package com.ccadmin.electronicinvoicing.exception;

import com.ccadmin.electronicinvoicing.dto.ResponseWsDto;
import java.util.Collections;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ResponseWsDto<Object>> handleApiException(ApiException ex) {
        ResponseWsDto<Object> response = new ResponseWsDto<>();
        response.ok = false;
        response.code = ex.getCode();
        response.message = ex.getMessage();
        response.errors.addAll(ex.getErrors());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseWsDto<Object>> handleValidation(MethodArgumentNotValidException ex) {
        ResponseWsDto<Object> response = new ResponseWsDto<>();
        response.ok = false;
        response.code = "APP-0002";
        response.message = "Validación incorrecta";
        ex.getBindingResult().getFieldErrors().forEach(error -> response.errors.add(error.getField() + ": " + error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseWsDto<Object>> handleGeneric(Exception ex) {
        ResponseWsDto<Object> response = new ResponseWsDto<>();
        response.ok = false;
        response.code = "APP-9999";
        response.message = "Error inesperado";
        response.errors.addAll(Collections.singletonList(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
