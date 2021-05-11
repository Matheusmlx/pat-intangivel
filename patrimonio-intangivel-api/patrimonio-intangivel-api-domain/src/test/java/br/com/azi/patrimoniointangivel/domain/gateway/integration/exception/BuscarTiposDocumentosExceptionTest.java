package br.com.azi.patrimoniointangivel.domain.gateway.integration.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BuscarTiposDocumentosExceptionTest {

    private final String message = "EXCEPTION";
    private final Throwable throwable = new Throwable();

    @Test
    public void deveExtenderRuntimException() {
        Assert.assertEquals(BuscarTiposDocumentosException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        BuscarTiposDocumentosException buscarTiposDocumentosException = new BuscarTiposDocumentosException(this.message);
        Assert.assertEquals(buscarTiposDocumentosException.getMessage(), this.message);
    }

    @Test
    public void deveSetarMensagemECausa() {
        BuscarTiposDocumentosException buscarTiposDocumentosException = new BuscarTiposDocumentosException(this.message, this.throwable);
        Assert.assertEquals(buscarTiposDocumentosException.getMessage(), this.message);
        Assert.assertEquals(buscarTiposDocumentosException.getCause(), this.throwable);
    }
}

