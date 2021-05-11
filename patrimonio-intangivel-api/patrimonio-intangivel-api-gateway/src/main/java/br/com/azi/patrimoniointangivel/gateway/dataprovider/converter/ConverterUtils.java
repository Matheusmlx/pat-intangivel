package br.com.azi.patrimoniointangivel.gateway.dataprovider.converter;

import java.util.Objects;

public class ConverterUtils {

    public static Integer shotToInteger(Short value) {
        if (Objects.nonNull(value)) {
            return value.intValue();
        }
        return null;
    }

    public static <T extends Enum<T>> Enum<T> stringToEnum(Class<T> clazz, String value) {
        if (Objects.nonNull(value)) {
            return Enum.valueOf(clazz, value);
        }
        return null;
    }
}
