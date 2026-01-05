package com.ccadmin.electronicinvoicing.util;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtil {
    private DateUtil() {
    }

    public static String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ISO_DATE);
    }

    public static String formatDateTime(OffsetDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
