package com.ccadmin.electronicinvoicing.entity;

public enum DocumentType {
    INVOICE("01"),
    BOLETA("03"),
    CREDIT_NOTE("07"),
    DEBIT_NOTE("08"),
    SUMMARY("SUMMARY"),
    VOIDED("VOIDED");

    private final String code;

    DocumentType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static DocumentType fromCode(String code) {
        for (DocumentType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
