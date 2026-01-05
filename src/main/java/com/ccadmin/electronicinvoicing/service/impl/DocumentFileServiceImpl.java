package com.ccadmin.electronicinvoicing.service.impl;

import com.ccadmin.electronicinvoicing.dto.ResponseWsDto;
import com.ccadmin.electronicinvoicing.entity.FileStoreEntity;
import com.ccadmin.electronicinvoicing.exception.ApiException;
import com.ccadmin.electronicinvoicing.repository.FileStoreRepository;
import com.ccadmin.electronicinvoicing.service.DocumentFileService;
import com.ccadmin.electronicinvoicing.service.FileStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentFileServiceImpl implements DocumentFileService {
    @Autowired
    private FileStoreRepository fileStoreRepository;

    @Autowired
    private FileStoreService fileStoreService;

    @Override
    public ResponseWsDto<byte[]> downloadXml(String documentId) {
        FileStoreEntity entity = fileStoreRepository.findById(documentId)
            .orElseThrow(() -> new ApiException("APP-0006", "Archivo XML no encontrado"));
        return buildResponse(entity.getXmlSignedPath());
    }

    @Override
    public ResponseWsDto<byte[]> downloadZip(String documentId) {
        FileStoreEntity entity = fileStoreRepository.findById(documentId)
            .orElseThrow(() -> new ApiException("APP-0007", "Archivo ZIP no encontrado"));
        return buildResponse(entity.getZipSentPath());
    }

    @Override
    public ResponseWsDto<byte[]> downloadCdr(String documentId) {
        FileStoreEntity entity = fileStoreRepository.findById(documentId)
            .orElseThrow(() -> new ApiException("APP-0008", "CDR no encontrado"));
        return buildResponse(entity.getCdrZipPath());
    }

    private ResponseWsDto<byte[]> buildResponse(String path) {
        if (path == null) {
            throw new ApiException("APP-0009", "Archivo no disponible");
        }
        ResponseWsDto<byte[]> response = new ResponseWsDto<>();
        response.ok = true;
        response.code = "APP-0000";
        response.message = "Archivo obtenido";
        response.data = fileStoreService.loadFile(path);
        return response;
    }
}
