package br.com.azi.patrimoniointangivel.utils.string;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class StringUtilsTest {

    @RunWith(MockitoJUnitRunner.class)
    public static class IsNotEmpty {
        @Test
        public void deveRetornarTrueQuandoAStringNaoForVazia() {
            final String string = "ABC";
            assertTrue(StringUtils.isNotEmpty(string));
        }

        @Test
        public void deveRetornarFalseQuandoAStringForVazia() {
            final String string = "";
            assertFalse(StringUtils.isNotEmpty(string));
        }

        @Test
        public void deveRetornarFalseQuandoAStringForNula() {
            final String string = null;
            assertFalse(StringUtils.isNotEmpty(string));
        }
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class PadLeftZeros {
        @Test
        public void devePreencherComZeros() {
            final String inputString = "1";
            final int lenght = 10;

            assertEquals(StringUtils.padLeftZeros(inputString, lenght), "0000000001");
        }
    }
}
