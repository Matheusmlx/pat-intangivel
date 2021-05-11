package br.com.azi.patrimoniointangivel.domain.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FiltroIncompletoExceptionTest {

    private final String message = "EXCEPTION";

    @Test
    public void deveExtenderRuntimException() {
        Assert.assertEquals(FiltroIncompletoException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        FiltroIncompletoException filtroIncompletoException = new FiltroIncompletoException(this.message);
        Assert.assertEquals(filtroIncompletoException.getMessage(), this.message);
    }
}
