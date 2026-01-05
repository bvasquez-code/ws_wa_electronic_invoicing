package com.ccadmin.electronicinvoicing.service;

public interface ZipService {
    byte[] zipSingle(String fileNameXml, byte[] xmlBytes, String zipName);
}
