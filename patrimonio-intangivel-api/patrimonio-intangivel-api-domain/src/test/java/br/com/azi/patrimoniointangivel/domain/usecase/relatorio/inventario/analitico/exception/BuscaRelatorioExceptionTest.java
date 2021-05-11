package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.analitico.exception;

import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.sintetico.exception.BuscaRelatorioException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BuscaRelatorioExceptionTest {

    private final String message = "Não há registros encontrados.";

    @Test
    public void deveExtenderRuntimExeption(){
        Assert.assertEquals(BuscaRelatorioException.class.getSuperclass().getName(),RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem(){
        BuscaRelatorioException buscaRelatorioException = new BuscaRelatorioException();
        Assert.assertEquals(buscaRelatorioException.getMessage(), this.message);
    }
}
