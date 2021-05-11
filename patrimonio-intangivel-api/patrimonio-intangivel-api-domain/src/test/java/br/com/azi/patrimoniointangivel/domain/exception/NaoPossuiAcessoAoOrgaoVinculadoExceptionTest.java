package br.com.azi.patrimoniointangivel.domain.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NaoPossuiAcessoAoOrgaoVinculadoExceptionTest {

    private final String message = "Você não possui acesso ao orgão vinculado a este patrimonio!";

    @Test
    public void deveExtenderRuntimException() {
        Assert.assertEquals(NaoPossuiAcessoAoOrgaoVinculadoException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        NaoPossuiAcessoAoOrgaoVinculadoException naoPossuiAcessoAoOrgaoVinculadoException = new NaoPossuiAcessoAoOrgaoVinculadoException();
        Assert.assertEquals(naoPossuiAcessoAoOrgaoVinculadoException.getMessage(), this.message);
    }
}

