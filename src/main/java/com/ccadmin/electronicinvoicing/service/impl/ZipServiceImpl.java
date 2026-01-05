package com.ccadmin.electronicinvoicing.service.impl;

import com.ccadmin.electronicinvoicing.service.ZipService;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.springframework.stereotype.Service;

@Service
public class ZipServiceImpl implements ZipService {
    @Override
    public byte[] zipSingle(String fileNameXml, byte[] xmlBytes, String zipName) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {
            ZipEntry entry = new ZipEntry(fileNameXml);
            zos.putNextEntry(entry);
            zos.write(xmlBytes);
            zos.closeEntry();
            zos.finish();
            return baos.toByteArray();
        } catch (Exception ex) {
            throw new IllegalStateException("No se pudo generar el ZIP", ex);
        }
    }
}
