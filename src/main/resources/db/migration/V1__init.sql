CREATE TABLE ei_issuer (
    id VARCHAR(36) PRIMARY KEY,
    ruc VARCHAR(11) NOT NULL UNIQUE,
    razon_social VARCHAR(255) NOT NULL,
    ubigeo VARCHAR(10) NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    usuario_sol VARCHAR(100) NULL,
    clave_sol VARCHAR(100) NULL,
    endpoint_profile VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE ei_document (
    id VARCHAR(36) PRIMARY KEY,
    issuer_id VARCHAR(36) NOT NULL,
    document_type VARCHAR(20) NOT NULL,
    serie VARCHAR(10) NOT NULL,
    correlativo VARCHAR(20) NOT NULL,
    issue_date DATE NOT NULL,
    currency VARCHAR(10) NOT NULL,
    customer_doc_type VARCHAR(10) NOT NULL,
    customer_doc_number VARCHAR(20) NOT NULL,
    customer_name VARCHAR(255) NOT NULL,
    op_gravada DECIMAL(18,2) NOT NULL,
    op_inafecta DECIMAL(18,2) NOT NULL,
    op_exonerada DECIMAL(18,2) NOT NULL,
    igv DECIMAL(18,2) NOT NULL,
    icbper DECIMAL(18,2) NOT NULL,
    total DECIMAL(18,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    sunat_ticket VARCHAR(50) NULL,
    sunat_code VARCHAR(20) NULL,
    sunat_message VARCHAR(255) NULL,
    hash_xml VARCHAR(255) NULL,
    hash_zip VARCHAR(255) NULL,
    hash_cdr VARCHAR(255) NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_document_issuer FOREIGN KEY (issuer_id) REFERENCES ei_issuer(id),
    CONSTRAINT uq_document UNIQUE (issuer_id, document_type, serie, correlativo)
);

CREATE TABLE ei_document_line (
    id VARCHAR(36) PRIMARY KEY,
    document_id VARCHAR(36) NOT NULL,
    item_number INT NOT NULL,
    sku VARCHAR(50) NULL,
    description VARCHAR(255) NOT NULL,
    unit VARCHAR(20) NOT NULL,
    quantity DECIMAL(18,2) NOT NULL,
    unit_value DECIMAL(18,2) NOT NULL,
    unit_price DECIMAL(18,2) NOT NULL,
    igv DECIMAL(18,2) NOT NULL,
    igv_affectation_type VARCHAR(10) NOT NULL,
    line_total DECIMAL(18,2) NOT NULL,
    CONSTRAINT fk_line_document FOREIGN KEY (document_id) REFERENCES ei_document(id)
);

CREATE TABLE ei_file_store (
    document_id VARCHAR(36) PRIMARY KEY,
    xml_signed_path VARCHAR(255) NULL,
    zip_sent_path VARCHAR(255) NULL,
    cdr_zip_path VARCHAR(255) NULL,
    cdr_xml_path VARCHAR(255) NULL,
    CONSTRAINT fk_file_document FOREIGN KEY (document_id) REFERENCES ei_document(id)
);

CREATE TABLE ei_audit_log (
    id VARCHAR(36) PRIMARY KEY,
    document_id VARCHAR(36) NULL,
    event VARCHAR(100) NOT NULL,
    payload_min VARCHAR(1000) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_audit_document FOREIGN KEY (document_id) REFERENCES ei_document(id)
);

CREATE TABLE ei_outbox (
    id VARCHAR(36) PRIMARY KEY,
    document_id VARCHAR(36) NOT NULL,
    action VARCHAR(30) NOT NULL,
    attempts INT NOT NULL,
    next_retry_at TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    last_error VARCHAR(255) NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
