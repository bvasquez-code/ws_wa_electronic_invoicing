package com.ccadmin.electronicinvoicing.exception;

import java.util.ArrayList;
import java.util.List;

public class ApiException extends RuntimeException {
    private final String code;
    private final List<String> errors = new ArrayList<>();

    public ApiException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ApiException(String code, String message, List<String> errors) {
        super(message);
        this.code = code;
        if (errors != null) {
            this.errors.addAll(errors);
        }
    }

    public String getCode() {
        return code;
    }

    public List<String> getErrors() {
        return errors;
    }
}
