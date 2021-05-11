package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.exception;

import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.ativar.exception.DataAtivacaoIgualDataDeVencimentoException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataAtivacaoIgualDataDeVencimentoExceptionTest {

    private final String MESSAGE = "A data de Vencimento deve ser posterior a data de Ativação";

    @Test
    public void deveRetornarAMensagemCorreta(){
        Assert.assertEquals(MESSAGE, new DataAtivacaoIgualDataDeVencimentoException().getMessage());
    }

}
