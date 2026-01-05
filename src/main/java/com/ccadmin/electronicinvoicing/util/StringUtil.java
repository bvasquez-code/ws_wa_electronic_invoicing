package com.ccadmin.electronicinvoicing.util;

public final class StringUtil {
    private StringUtil() {
    }

    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    public static String nvl(String value, String defaultValue) {
        return isEmpty(value) ? defaultValue : value;
    }

    public static boolean hasExactLength(String value, int length) {
        return value != null && value.length() == length;
    }

    public static boolean lengthBetween(String value, int min, int max) {
        return value != null && value.length() >= min && value.length() <= max;
    }
}
