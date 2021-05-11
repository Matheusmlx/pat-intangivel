package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscar.converter;

import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscar.BuscarContasContabeisInputData;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class BuscarContasContabeisFiltroConverterTest{

    @Mock
    private BuscarContasContabeisFiltroConverter buscarContasContabeisFiltroConverter;
    private ContaContabil.Filtro contaContabilFiltro = new ContaContabil.Filtro();
    private BuscarContasContabeisInputData buscarContasContabeisInputData = new BuscarContasContabeisInputData();

    @Test
    public void testTo() {
        this.contaContabilFiltro.setPage((long) 2);
        Mockito.when(buscarContasContabeisFiltroConverter
            .superTo(this.buscarContasContabeisInputData))
            .thenReturn(contaContabilFiltro);
        Mockito.when(buscarContasContabeisFiltroConverter
            .to(Mockito.any(BuscarContasContabeisInputData.class)))
            .thenCallRealMethod();
        this.contaContabilFiltro = this.buscarContasContabeisFiltroConverter.to(this.buscarContasContabeisInputData);
        Assert.assertEquals(Optional.ofNullable(this.contaContabilFiltro.getPage()), Optional.of((long) 1));
    }
}
