package br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.geraamrotizacaomensal.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RodarAmortizacaoExceptionTest{

    private final String message = "Erro ao rodar amortização!";

    @Test
    public void deveSetarMensagem() {
        RodarAmortizacaoException rodarAmortizacaoException = new RodarAmortizacaoException();
        Assert.assertEquals(rodarAmortizacaoException.getMessage(), this.message);
    }
}
