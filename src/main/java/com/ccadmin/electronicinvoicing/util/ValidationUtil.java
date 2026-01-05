package com.ccadmin.electronicinvoicing.util;

import java.util.regex.Pattern;

public final class ValidationUtil {
    private static final Pattern RUC_PATTERN = Pattern.compile("\\d{11}");
    private static final Pattern SERIE_PATTERN = Pattern.compile("[A-Z0-9]{1,4}");
    private static final Pattern CORRELATIVO_PATTERN = Pattern.compile("\\d{1,8}");

    private ValidationUtil() {
    }

    public static boolean isValidRuc(String ruc) {
        return StringUtil.isNotEmpty(ruc) && RUC_PATTERN.matcher(ruc).matches();
    }

    public static boolean isValidSerie(String serie) {
        return StringUtil.isNotEmpty(serie) && SERIE_PATTERN.matcher(serie).matches();
    }

    public static boolean isValidCorrelativo(String correlativo) {
        return StringUtil.isNotEmpty(correlativo) && CORRELATIVO_PATTERN.matcher(correlativo).matches();
    }
}
