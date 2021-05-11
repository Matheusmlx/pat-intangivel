package br.com.azi.patrimoniointangivel.domain.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PatrimonioNaoEncontradoExceptionTest{

    private final String message = "Patrimonio não encontrado!";

    @Test
    public void deveExtenderRuntimException() {
        Assert.assertEquals(PatrimonioNaoEncontradoException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        PatrimonioNaoEncontradoException patrimonioNaoEncontradoException = new PatrimonioNaoEncontradoException();
        Assert.assertEquals(patrimonioNaoEncontradoException.getMessage(), this.message);
    }
}
