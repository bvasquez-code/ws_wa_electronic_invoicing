package com.ccadmin.electronicinvoicing.service;

import com.ccadmin.electronicinvoicing.entity.ElectronicDocumentEntity;
import com.ccadmin.electronicinvoicing.entity.FileStoreEntity;

public interface FileStoreService {
    FileStoreEntity storeSignedXml(ElectronicDocumentEntity document, byte[] xmlBytes, String fileName);

    FileStoreEntity storeZip(ElectronicDocumentEntity document, byte[] zipBytes, String fileName);

    FileStoreEntity storeCdrZip(ElectronicDocumentEntity document, byte[] zipBytes, String fileName);

    byte[] loadFile(String path);
}
