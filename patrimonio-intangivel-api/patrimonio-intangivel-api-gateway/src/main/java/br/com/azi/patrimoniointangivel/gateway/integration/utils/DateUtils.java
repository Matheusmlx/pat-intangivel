package br.com.azi.patrimoniointangivel.gateway.integration.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {

    public static Date localDateTimeToDate(LocalDateTime dateToConvert) {
        return java.util.Date
            .from(dateToConvert.atZone(ZoneId.systemDefault())
                .toInstant());
    }
}
