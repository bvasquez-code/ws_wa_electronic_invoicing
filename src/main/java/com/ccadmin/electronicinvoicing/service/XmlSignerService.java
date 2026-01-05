package com.ccadmin.electronicinvoicing.service;

import com.ccadmin.electronicinvoicing.dto.IssuerConfig;

public interface XmlSignerService {
    String signXml(String unsignedXml, IssuerConfig config);
}
