package br.com.azi.patrimoniointangivel.domain.gateway.integration.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PromoteArquivoExceptionTest{

    private final String message = "EXCEPTION";
    private final Throwable throwable = new Throwable();

    @Test
    public void deveExtenderRuntimException() {
        Assert.assertEquals(PromoteArquivoException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        PromoteArquivoException promoteArquivoException = new PromoteArquivoException(this.message);
        Assert.assertEquals(promoteArquivoException.getMessage(), this.message);
    }

    @Test
    public void deveSetarMensagemECausa() {
        PromoteArquivoException promoteArquivoException = new PromoteArquivoException(this.message, this.throwable);
        Assert.assertEquals(promoteArquivoException.getMessage(), this.message);
        Assert.assertEquals(promoteArquivoException.getCause(), this.throwable);
    }
}
