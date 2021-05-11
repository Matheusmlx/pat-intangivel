package br.com.azi.patrimoniointangivel.domain.gateway.integration.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BuscaMetadataArquivoExceptionTest{

    private final String message = "EXCEPTION";
    private final Throwable throwable = new Throwable();

    @Test
    public void deveExtenderRuntimException() {
        Assert.assertEquals(BuscaMetadataArquivoException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagemECausa() {
        BuscaMetadataArquivoException buscaMetadataArquivoException = new BuscaMetadataArquivoException(this.message, this.throwable);
        Assert.assertEquals(buscaMetadataArquivoException.getMessage(), this.message);
        Assert.assertEquals(buscaMetadataArquivoException.getCause(), this.throwable);
    }
}

