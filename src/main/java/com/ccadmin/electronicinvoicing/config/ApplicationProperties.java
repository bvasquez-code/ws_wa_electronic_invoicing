package com.ccadmin.electronicinvoicing.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {
    private Storage storage = new Storage();
    private Keystore keystore = new Keystore();
    private Sunat sunat = new Sunat();
    private Outbox outbox = new Outbox();

    @Data
    public static class Storage {
        private String basePath;
        private String xmlPath;
        private String zipPath;
        private String cdrPath;
    }

    @Data
    public static class Keystore {
        private String path;
        private String password;
    }

    @Data
    public static class Sunat {
        private boolean mockEnabled;
        private String betaEndpoint;
        private String prodEndpoint;
    }

    @Data
    public static class Outbox {
        private long fixedDelayMs = 15000L;
        private int maxAttempts = 3;
    }
}
