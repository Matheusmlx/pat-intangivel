package br.com.azi.patrimoniointangivel.domain.gateway.integration.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DownloadArquivoExceptionTest{

    private final String message = "EXCEPTION";
    private final Throwable throwable = new Throwable();

    @Test
    public void deveExtenderRuntimException() {
        Assert.assertEquals(DownloadArquivoException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        DownloadArquivoException downloadArquivoException = new DownloadArquivoException(this.message);
        Assert.assertEquals(downloadArquivoException.getMessage(), this.message);
    }

    @Test
    public void deveSetarMensagemECausa() {
        DownloadArquivoException downloadArquivoException = new DownloadArquivoException(this.message, this.throwable);
        Assert.assertEquals(downloadArquivoException.getMessage(), this.message);
        Assert.assertEquals(downloadArquivoException.getCause(), this.throwable);
    }
}
