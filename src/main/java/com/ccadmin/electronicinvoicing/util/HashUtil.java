package com.ccadmin.electronicinvoicing.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public final class HashUtil {
    private HashUtil() {
    }

    public static String sha256(byte[] input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input);
            StringBuilder builder = new StringBuilder();
            for (byte b : hash) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (Exception ex) {
            throw new IllegalStateException("No se pudo calcular el hash SHA-256", ex);
        }
    }

    public static String sha256(String input) {
        return sha256(input.getBytes(StandardCharsets.UTF_8));
    }
}
