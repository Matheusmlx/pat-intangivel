package br.com.azi.patrimoniointangivel.domain.gateway.integration.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BuscarFornecedorExceptionTest{

    private final String message = "EXCEPTION";
    private final Throwable throwable = new Throwable();

    @Test
    public void deveExtenderRuntimException() {
        Assert.assertEquals(BuscarFornecedorException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        BuscarFornecedorException buscarFornecedorException = new BuscarFornecedorException(this.message);
        Assert.assertEquals(buscarFornecedorException.getMessage(), this.message);
    }

    @Test
    public void deveSetarMensagemECausa() {
        BuscarFornecedorException buscarFornecedorException = new BuscarFornecedorException(this.message, this.throwable);
        Assert.assertEquals(buscarFornecedorException.getMessage(), this.message);
        Assert.assertEquals(buscarFornecedorException.getCause(), this.throwable);
    }
}
