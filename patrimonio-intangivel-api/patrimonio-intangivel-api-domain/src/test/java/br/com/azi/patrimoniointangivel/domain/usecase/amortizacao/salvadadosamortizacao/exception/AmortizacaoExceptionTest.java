package br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.salvadadosamortizacao.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AmortizacaoExceptionTest{

    private final String message = "Vida Util Não Válida!";

    @Test
    public void deveSetarMensagem() {
        AmortizacaoException amortizacaoException = new AmortizacaoException();
        Assert.assertEquals(amortizacaoException.getMessage(), this.message);
    }
}

