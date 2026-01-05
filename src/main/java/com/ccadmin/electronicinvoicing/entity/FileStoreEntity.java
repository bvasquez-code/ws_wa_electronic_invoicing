package com.ccadmin.electronicinvoicing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "ei_file_store")
public class FileStoreEntity {
    @Id
    @Column(length = 36, name = "document_id")
    private String documentId;

    @OneToOne
    @JoinColumn(name = "document_id", insertable = false, updatable = false)
    private ElectronicDocumentEntity document;

    @Column(name = "xml_signed_path")
    private String xmlSignedPath;

    @Column(name = "zip_sent_path")
    private String zipSentPath;

    @Column(name = "cdr_zip_path")
    private String cdrZipPath;

    @Column(name = "cdr_xml_path")
    private String cdrXmlPath;
}
