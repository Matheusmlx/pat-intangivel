package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.analitico.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AmortizacaoEmAndamentoExceptionTest {

    private final String message = "Inventário não pode ser gerado porque a rotina de amortização está rodando. Tente novamente em algumas horas.";

    @Test
    public void deveExtenderRuntimException(){
        Assert.assertEquals(AmortizacaoEmAndamentoException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        AmortizacaoEmAndamentoException amortizacaoEmAndamentoException = new AmortizacaoEmAndamentoException();
        Assert.assertEquals(amortizacaoEmAndamentoException.getMessage(), this.message);
    }
}
