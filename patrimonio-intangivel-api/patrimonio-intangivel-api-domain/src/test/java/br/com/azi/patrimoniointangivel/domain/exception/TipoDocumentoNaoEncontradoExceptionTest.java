package br.com.azi.patrimoniointangivel.domain.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TipoDocumentoNaoEncontradoExceptionTest{

    private final String message = "Tipo de documento não encontrado!";

    @Test
    public void deveExtenderRuntimException() {
        Assert.assertEquals(TipoDocumentoNaoEncontradoException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        TipoDocumentoNaoEncontradoException tipoDocumentoNaoEncontradoException = new TipoDocumentoNaoEncontradoException();
        Assert.assertEquals(tipoDocumentoNaoEncontradoException.getMessage(), this.message);
    }
}
