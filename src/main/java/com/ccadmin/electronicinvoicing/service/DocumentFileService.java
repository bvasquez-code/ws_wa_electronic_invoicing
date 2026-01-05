package com.ccadmin.electronicinvoicing.service;

import com.ccadmin.electronicinvoicing.dto.ResponseWsDto;

public interface DocumentFileService {
    ResponseWsDto<byte[]> downloadXml(String documentId);

    ResponseWsDto<byte[]> downloadZip(String documentId);

    ResponseWsDto<byte[]> downloadCdr(String documentId);
}
