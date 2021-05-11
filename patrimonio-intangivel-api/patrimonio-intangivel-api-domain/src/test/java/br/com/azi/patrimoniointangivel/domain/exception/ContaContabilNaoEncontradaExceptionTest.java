package br.com.azi.patrimoniointangivel.domain.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ContaContabilNaoEncontradaExceptionTest{

    private final String message = "Conta contábil não encontrada!";

    @Test
    public void deveExtenderRuntimException() {
        Assert.assertEquals(ContaContabilNaoEncontradaException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        ContaContabilNaoEncontradaException contaContabilNaoEncontradaException = new ContaContabilNaoEncontradaException();
        Assert.assertEquals(contaContabilNaoEncontradaException.getMessage(), this.message);
    }
}
