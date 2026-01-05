package com.ccadmin.electronicinvoicing.service.impl;

import com.ccadmin.electronicinvoicing.dto.IssuerConfig;
import com.ccadmin.electronicinvoicing.exception.ApiException;
import com.ccadmin.electronicinvoicing.service.XmlSignerService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

@Service
public class XmlSignerServiceImpl implements XmlSignerService {
    @Override
    public String signXml(String unsignedXml, IssuerConfig config) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document document = factory.newDocumentBuilder().parse(new ByteArrayInputStream(unsignedXml.getBytes(StandardCharsets.UTF_8)));

            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (FileInputStream fis = new FileInputStream(config.getKeystorePath())) {
                keyStore.load(fis, config.getKeystorePassword().toCharArray());
            }
            String alias = keyStore.aliases().nextElement();
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, config.getKeystorePassword().toCharArray());
            X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);

            XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");
            Reference reference = signatureFactory.newReference("",
                signatureFactory.newDigestMethod(DigestMethod.SHA256, null),
                java.util.Collections.singletonList(signatureFactory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)),
                null,
                null);

            SignedInfo signedInfo = signatureFactory.newSignedInfo(
                signatureFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
                signatureFactory.newSignatureMethod(SignatureMethod.RSA_SHA256, null),
                java.util.Collections.singletonList(reference));

            KeyInfoFactory keyInfoFactory = signatureFactory.getKeyInfoFactory();
            X509Data x509Data = keyInfoFactory.newX509Data(java.util.Collections.singletonList(certificate));
            KeyInfo keyInfo = keyInfoFactory.newKeyInfo(java.util.Collections.singletonList(x509Data));

            DOMSignContext signContext = new DOMSignContext(privateKey, document.getDocumentElement());
            XMLSignature signature = signatureFactory.newXMLSignature(signedInfo, keyInfo);
            signature.sign(signContext);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            transformer.transform(new DOMSource(document), new StreamResult(outputStream));
            return outputStream.toString(StandardCharsets.UTF_8);
        } catch (java.io.FileNotFoundException ex) {
            throw new ApiException("APP-0101", "Certificado no encontrado");
        } catch (java.security.UnrecoverableKeyException ex) {
            throw new ApiException("APP-0102", "Contraseña incorrecta");
        } catch (Exception ex) {
            throw new ApiException("APP-0100", "Certificado inválido", java.util.List.of(ex.getMessage()));
        }
    }
}
