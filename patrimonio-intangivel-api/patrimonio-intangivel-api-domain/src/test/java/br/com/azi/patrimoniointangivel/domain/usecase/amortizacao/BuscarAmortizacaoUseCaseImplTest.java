package br.com.azi.patrimoniointangivel.domain.usecase.amortizacao;

import br.com.azi.patrimoniointangivel.domain.entity.Amortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.AmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.buscarlistagem.BuscarAmortizacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.buscarlistagem.BuscarAmortizacaoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.buscarlistagem.BuscarAmortizacaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.buscarlistagem.converter.BuscarAmortizacaoOutputDataConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BuscarAmortizacaoUseCaseImplTest {

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoBuscaSemId() {
        BuscarAmortizacaoUseCaseImpl useCase = new BuscarAmortizacaoUseCaseImpl(
            Mockito.mock(AmortizacaoDataProvider.class),
            new BuscarAmortizacaoOutputDataConverter());

        BuscarAmortizacaoInputData inputData = new BuscarAmortizacaoInputData();
        useCase.executar(inputData);
    }

    @Test
    public void deveBuscarAmortizacao() {
        AmortizacaoDataProvider amortizacaoDataProvider = Mockito.mock(AmortizacaoDataProvider.class);
        BuscarAmortizacaoUseCaseImpl useCase = new BuscarAmortizacaoUseCaseImpl(
            amortizacaoDataProvider,
            new BuscarAmortizacaoOutputDataConverter());

        Mockito.when(amortizacaoDataProvider.buscar(any(Long.class)))
            .thenReturn(
                        List.of(
                            Amortizacao.builder()
                                .id(1L)
                                .dataInicial(LocalDateTime .of(2020, 1,1,0,0,0))
                                .dataFinal(LocalDateTime .of(2020, 1,31,23,59,59))
                                .valorAnterior(BigDecimal.valueOf(100))
                                .valorPosterior(BigDecimal.valueOf(90))
                                .valorSubtraido(BigDecimal.TEN)
                                .taxaAplicada(BigDecimal.TEN)
                                .orgao(UnidadeOrganizacional.builder().id(1L).sigla("GP").nome("nome GP").build())
                                .build(),
                            Amortizacao.builder()
                                .id(2L)
                                .dataInicial(LocalDateTime .of(2020, 2,1,0,0,0))
                                .dataFinal(LocalDateTime .of(2020, 2,29,23,59,59))
                                .valorAnterior(BigDecimal.valueOf(90))
                                .valorPosterior(BigDecimal.valueOf(80))
                                .valorSubtraido(BigDecimal.TEN)
                                .taxaAplicada(BigDecimal.TEN)
                                .orgao(UnidadeOrganizacional.builder().id(2L).sigla("GP2").nome("nome GP2").build())
                                .build()
                        ));

        BuscarAmortizacaoInputData inputData = new BuscarAmortizacaoInputData(1L);
        BuscarAmortizacaoOutputData outputData = useCase.executar(inputData);

        Assert.assertTrue(outputData.getItems() instanceof ArrayList);
        Assert.assertFalse(outputData.getItems().isEmpty());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.getItems().get(0).getId());
        Assert.assertEquals(java.util.Optional.of(2L).get(), outputData.getItems().get(1).getId());
        Assert.assertEquals(LocalDateTime .of(2020, 1,1,0,0,0), outputData.getItems().get(0).getDataInicial());
        Assert.assertEquals(LocalDateTime .of(2020, 1,31,23,59,59), outputData.getItems().get(0).getDataFinal());
        Assert.assertEquals(BigDecimal.valueOf(100), outputData.getItems().get(0).getValorAnterior());
        Assert.assertEquals(BigDecimal.valueOf(90), outputData.getItems().get(0).getValorPosterior());
        Assert.assertEquals(BigDecimal.TEN, outputData.getItems().get(0).getValorSubtraido());
        Assert.assertEquals(BigDecimal.TEN, outputData.getItems().get(0).getTaxaAplicada());
        Assert.assertEquals(java.util.Optional.of("GP").get(), outputData.getItems().get(0).getOrgaoSigla());
        Assert.assertEquals(java.util.Optional.of("GP2").get(), outputData.getItems().get(1).getOrgaoSigla());

    }

    @Test
    public void deveRetornarListaVaziaQuandoNãoHouverAmortizacao() {
        AmortizacaoDataProvider amortizacaoDataProvider = Mockito.mock(AmortizacaoDataProvider.class);
        BuscarAmortizacaoUseCaseImpl useCase = new BuscarAmortizacaoUseCaseImpl(
            amortizacaoDataProvider,
            new BuscarAmortizacaoOutputDataConverter());

        Mockito.when(amortizacaoDataProvider.buscar(any(Long.class)))
            .thenReturn(Collections.emptyList());

        BuscarAmortizacaoInputData inputData = new BuscarAmortizacaoInputData(1L);
        BuscarAmortizacaoOutputData outputData = useCase.executar(inputData);

        Assert.assertTrue(outputData.getItems() instanceof ArrayList);
        Assert.assertTrue(outputData.getItems().isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdForNulo() {
        AmortizacaoDataProvider amortizacaoDataProvider = Mockito.mock(AmortizacaoDataProvider.class);
        BuscarAmortizacaoUseCaseImpl useCase = new BuscarAmortizacaoUseCaseImpl(
            amortizacaoDataProvider,
            new BuscarAmortizacaoOutputDataConverter());

        BuscarAmortizacaoInputData inputData = new BuscarAmortizacaoInputData();
        inputData.setId(null);
        BuscarAmortizacaoOutputData outputData = useCase.executar(inputData);

        Assert.assertTrue(outputData.getItems() instanceof ArrayList);
        Assert.assertTrue(outputData.getItems().isEmpty());
    }

}
