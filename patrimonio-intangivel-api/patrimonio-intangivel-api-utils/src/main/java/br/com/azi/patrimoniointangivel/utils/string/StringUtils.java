package br.com.azi.patrimoniointangivel.utils.string;

import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import java.util.Objects;

public class StringUtils {

    public static String formatString(String value, String pattern) {
        MaskFormatter mf;
        try {
            mf = new MaskFormatter(pattern);
            mf.setValueContainsLiteralCharacters(false);
            return mf.valueToString(value);
        } catch (ParseException ex) {
            return value;
        }
    }

    public static boolean isNotEmpty(String string) {
        return (Objects.nonNull(string) && !string.isEmpty());
    }

    public static String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }
}
