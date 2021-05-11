package br.com.azi.patrimoniointangivel.domain.gateway.integration.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BuscarUnidadesOrganizacionaisExceptionTest {

    private final String message = "EXCEPTION";
    private final Throwable throwable = new Throwable();

    @Test
    public void deveExtenderRuntimException() {
        Assert.assertEquals(BuscarUnidadesOrganizacionaisException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        BuscarUnidadesOrganizacionaisException buscarUnidadesOrganizacionaisException = new BuscarUnidadesOrganizacionaisException(this.message);
        Assert.assertEquals(buscarUnidadesOrganizacionaisException.getMessage(), this.message);
    }

    @Test
    public void deveSetarMensagemECausa() {
        BuscarUnidadesOrganizacionaisException buscarUnidadesOrganizacionaisException = new BuscarUnidadesOrganizacionaisException(this.message, this.throwable);
        Assert.assertEquals(buscarUnidadesOrganizacionaisException.getMessage(), this.message);
        Assert.assertEquals(buscarUnidadesOrganizacionaisException.getCause(), this.throwable);
    }
}
