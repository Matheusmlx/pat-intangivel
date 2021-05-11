package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.busca;

import br.com.azi.patrimoniointangivel.domain.entity.ConfigContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.ListaPaginada;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscar.BuscarContasContabeisInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscar.BuscarContasContabeisOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscar.BuscarContasContabeisUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscar.converter.BuscarContasContabeisFiltroConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscar.converter.BuscarContasContabeisOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscar.exception.FiltroIncompletoException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

public class BuscarContasContabeisUseCaseImplTest {

    @Test
    public void deveFalharQuandoNaoTemFiltro() {
        BuscarContasContabeisInputData inputData = new BuscarContasContabeisInputData();
        inputData.setSort("ASC");

        try {
            new BuscarContasContabeisUseCaseImpl(null, null, null, null, null).executar(inputData);
        } catch (Exception e) {
            Assert.assertEquals(2, e.getSuppressed().length);
            Assert.assertTrue(e.getSuppressed()[0] instanceof FiltroIncompletoException);
        }
    }

    @Test
    public void deveBuscarListagemComFiltrosCorretos() {
        ContaContabilDataProvider contaContabilDataProvider = Mockito.mock(ContaContabilDataProvider.class);
        ConfigContaContabilDataProvider configContaContabilDataProvider = Mockito.mock(ConfigContaContabilDataProvider.class);
        Long produtoId = 1L;
        BuscarContasContabeisFiltroConverter filtroConverter = Mockito.mock(BuscarContasContabeisFiltroConverter.class);

        BuscarContasContabeisUseCaseImpl useCase = new BuscarContasContabeisUseCaseImpl(
            contaContabilDataProvider,
            configContaContabilDataProvider,
            produtoId,
            filtroConverter,
            new BuscarContasContabeisOutputDataConverter()
        );

        BuscarContasContabeisInputData inputData = new BuscarContasContabeisInputData(0L, 10L, "ASC", "", "Software", 1L);

        Mockito.when(contaContabilDataProvider.buscarPorFiltro(any(ContaContabil.Filtro.class)))
            .thenReturn(
                ListaPaginada.<ContaContabil>builder()
                    .items(
                        List.of(ContaContabil.builder()
                            .id(1L)
                            .codigo("090809448")
                            .descricao("Conta Contabil")
                            .situacao(ContaContabil.Situacao.ATIVO)
                            .build())
                    )
                    .totalElements(1L)
                    .totalPages(1L)
                    .build()
            );

        Mockito.when(configContaContabilDataProvider.buscarAtualPorContaContabil(any(Long.class)))
            .thenReturn(Optional.of(
                ConfigContaContabil.builder()
                    .id(2L)
                    .tipo(ConfigContaContabil.Tipo.AMORTIZAVEL)
                    .metodo(ConfigContaContabil.Metodo.QUOTAS_CONSTANTES)
                    .build()
            ));

        Mockito.when(filtroConverter.to(any(BuscarContasContabeisInputData.class)))
            .thenReturn(ContaContabil.Filtro.builder()
                .conteudo("Software")
                .produtoId(1L).build());

        BuscarContasContabeisOutputData outputData = useCase.executar(inputData);

        Assert.assertTrue(outputData.getItems() instanceof ArrayList);
        Assert.assertEquals(outputData.getItems().size(), 1);
        Assert.assertEquals(outputData.getItems().get(0).getNome(), "Conta Contabil");
        Assert.assertEquals(outputData.getItems().get(0).getCodigo(), "090809448");
        Assert.assertEquals(outputData.getItems().get(0).getMetodo(), "QUOTAS_CONSTANTES");
        Assert.assertEquals(outputData.getItems().get(0).getIdConfigAmortizacao(), Long.valueOf(2));
        Assert.assertEquals(outputData.getItems().get(0).getId(), Long.valueOf(1));
        Assert.assertEquals(outputData.getItems().get(0).getTipo(), "AMORTIZAVEL");
        Assert.assertEquals(outputData.getTotalElements(), Long.valueOf(1));
        Assert.assertEquals(outputData.getTotalPages(), Long.valueOf(1));
    }
}
