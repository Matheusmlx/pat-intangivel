package br.com.azi.patrimoniointangivel.domain.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DocumentoNaoEncontradoExceptionTest{

    private final String message = "Documento não encontrado!";

    @Test
    public void deveExtenderRuntimException() {
        Assert.assertEquals(DocumentoNaoEncontradoException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        DocumentoNaoEncontradoException documentoNaoEncontradoException = new DocumentoNaoEncontradoException();
        Assert.assertEquals(documentoNaoEncontradoException.getMessage(), this.message);
    }
}
