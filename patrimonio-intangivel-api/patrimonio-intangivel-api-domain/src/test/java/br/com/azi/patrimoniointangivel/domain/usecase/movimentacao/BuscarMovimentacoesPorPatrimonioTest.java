package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao;

import br.com.azi.patrimoniointangivel.domain.entity.ListaPaginada;
import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporpatrimonio.BuscarMovimentacoesPorPatrimonioInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporpatrimonio.BuscarMovimentacoesPorPatrimonioOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporpatrimonio.BuscarMovimentacoesPorPatrimonioUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporpatrimonio.converter.BuscarMovimentacoesPorPatrimonioOutputDataConverter;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class BuscarMovimentacoesPorPatrimonioTest {

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoTiverIdPatrimonio(){
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        MovimentacaoDataProvider movimentacaoDataProvider = Mockito.mock(MovimentacaoDataProvider.class);

        BuscarMovimentacoesPorPatrimonioInputData inputData = new BuscarMovimentacoesPorPatrimonioInputData();

        BuscarMovimentacoesPorPatrimonioUseCaseImpl useCase = new BuscarMovimentacoesPorPatrimonioUseCaseImpl(patrimonioDataProvider,
            movimentacaoDataProvider,
            new BuscarMovimentacoesPorPatrimonioOutputDataConverter());

        useCase.executar(inputData);
    }

    @Test
    public void deveBuscarListagem(){
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        MovimentacaoDataProvider movimentacaoDataProvider = Mockito.mock(MovimentacaoDataProvider.class);

        BuscarMovimentacoesPorPatrimonioUseCaseImpl useCase = new BuscarMovimentacoesPorPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            movimentacaoDataProvider,
            new BuscarMovimentacoesPorPatrimonioOutputDataConverter());

        BuscarMovimentacoesPorPatrimonioInputData inputData = new BuscarMovimentacoesPorPatrimonioInputData();
        inputData.setIdPatrimonio(1L);
        inputData.setTamanho(5L);

        Mockito.when(patrimonioDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(movimentacaoDataProvider.buscarMovimentacoesPorPatrimonio(inputData.getIdPatrimonio(), inputData.getTamanho()))
            .thenReturn(
                ListaPaginada.<Movimentacao>builder()
                    .items(
                        List.of(
                            Movimentacao.builder()
                                .id(1L)
                                .codigo("001")
                                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                                .motivo("Patrimonio está sem uso no orgão")
                                .tipo(Movimentacao.Tipo.DOACAO_ENTRE_ORGAOS)
                            .build(),
                            Movimentacao.builder()
                                .id(2L)
                                .codigo("002")
                                .situacao(Movimentacao.Situacao.AGUARDANDO_RECEBIMENTO)
                                .orgaoOrigem(UnidadeOrganizacional.builder()
                                    .id(1L)
                                    .descricao("AEMS-MS Agencia Estadual de Metrologia")
                                    .nome("Agência Estadual de Metrologia")
                                    .sigla("AEMS-MS")
                                    .build())
                                .orgaoDestino(UnidadeOrganizacional.builder()
                                    .id(2L)
                                    .descricao("DPGE - Defensoria Pública Geral do Estado")
                                    .nome("Defensoria Pública Geral do Estado")
                                    .sigla("DPGE")
                                    .build())
                                .tipo(Movimentacao.Tipo.DOACAO_PARA_TERCEIROS)
                                .situacao(Movimentacao.Situacao.FINALIZADO)
                                .motivo("Patrimonio em falta no orgão ")
                            .build())
                    )
                    .totalElements(2L)
                    .build()
        );

        BuscarMovimentacoesPorPatrimonioOutputData outputData = useCase.executar(inputData);

        Assert.assertTrue(outputData.getItems() instanceof ArrayList);
        Assert.assertFalse(outputData.getItems().isEmpty());
        Assert.assertEquals(java.util.Optional.of(1L).get(),outputData.getItems().get(0).getId());
        Assert.assertEquals(java.util.Optional.of(2L).get(),outputData.getItems().get(1).getId());
        Assert.assertEquals("EM_ELABORACAO", outputData.getItems().get(0).getSituacao());
        Assert.assertEquals("FINALIZADO", outputData.getItems().get(1).getSituacao());
    }
    @Test(expected = PatrimonioNaoEncontradoException.class)
    public void deveFalharQuandoMovimentacaoNaoExistir() {
        MovimentacaoDataProvider movimentacaoDataProvider = Mockito.mock(MovimentacaoDataProvider.class);
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        BuscarMovimentacoesPorPatrimonioOutputDataConverter outputDataConverter = new BuscarMovimentacoesPorPatrimonioOutputDataConverter();

        BuscarMovimentacoesPorPatrimonioUseCaseImpl useCase = new BuscarMovimentacoesPorPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            movimentacaoDataProvider,
            outputDataConverter
        );

        BuscarMovimentacoesPorPatrimonioInputData inputData = new BuscarMovimentacoesPorPatrimonioInputData();
        inputData.setIdPatrimonio(1L);

        Mockito.when(patrimonioDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.FALSE);

        useCase.executar(inputData);
    }
}
