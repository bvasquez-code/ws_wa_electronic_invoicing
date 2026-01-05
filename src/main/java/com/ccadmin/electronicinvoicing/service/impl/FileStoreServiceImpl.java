package com.ccadmin.electronicinvoicing.service.impl;

import com.ccadmin.electronicinvoicing.config.ApplicationProperties;
import com.ccadmin.electronicinvoicing.entity.ElectronicDocumentEntity;
import com.ccadmin.electronicinvoicing.entity.FileStoreEntity;
import com.ccadmin.electronicinvoicing.repository.FileStoreRepository;
import com.ccadmin.electronicinvoicing.service.FileStoreService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileStoreServiceImpl implements FileStoreService {
    @Autowired
    private ApplicationProperties properties;

    @Autowired
    private FileStoreRepository fileStoreRepository;

    @Override
    public FileStoreEntity storeSignedXml(ElectronicDocumentEntity document, byte[] xmlBytes, String fileName) {
        String path = resolvePath(document, properties.getStorage().getXmlPath(), fileName);
        writeFile(path, xmlBytes);
        FileStoreEntity entity = findOrCreate(document);
        entity.setXmlSignedPath(path);
        return fileStoreRepository.save(entity);
    }

    @Override
    public FileStoreEntity storeZip(ElectronicDocumentEntity document, byte[] zipBytes, String fileName) {
        String path = resolvePath(document, properties.getStorage().getZipPath(), fileName);
        writeFile(path, zipBytes);
        FileStoreEntity entity = findOrCreate(document);
        entity.setZipSentPath(path);
        return fileStoreRepository.save(entity);
    }

    @Override
    public FileStoreEntity storeCdrZip(ElectronicDocumentEntity document, byte[] zipBytes, String fileName) {
        String path = resolvePath(document, properties.getStorage().getCdrPath(), fileName);
        writeFile(path, zipBytes);
        FileStoreEntity entity = findOrCreate(document);
        entity.setCdrZipPath(path);
        return fileStoreRepository.save(entity);
    }

    @Override
    public byte[] loadFile(String path) {
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo leer el archivo almacenado", ex);
        }
    }

    private String resolvePath(ElectronicDocumentEntity document, String folder, String fileName) {
        String basePath = properties.getStorage().getBasePath();
        String datePart = document.getIssueDate().format(DateTimeFormatter.BASIC_ISO_DATE);
        Path path = Paths.get(basePath, folder, document.getIssuer().getRuc(), datePart);
        try {
            Files.createDirectories(path);
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo crear el directorio de almacenamiento", ex);
        }
        return path.resolve(fileName).toString();
    }

    private void writeFile(String path, byte[] content) {
        try {
            Files.write(Paths.get(path), content);
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo guardar el archivo", ex);
        }
    }

    private FileStoreEntity findOrCreate(ElectronicDocumentEntity document) {
        return fileStoreRepository.findById(document.getId()).orElseGet(() -> {
            FileStoreEntity entity = new FileStoreEntity();
            entity.setDocumentId(document.getId());
            entity.setDocument(document);
            return entity;
        });
    }
}
