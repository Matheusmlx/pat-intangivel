package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.edicao.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataAtivacaoMenorQueDataAquisicaoExceptionTest {

    private final String MESSAGE = "A data de ativação não pode ser menor que data de aquisição";

    @Test
    public void deveRetornarAMensagemCorreta() {
        Assert.assertEquals(MESSAGE, new DataAtivacaoMenorQueDataAquisicaoException().getMessage());
    }
}
