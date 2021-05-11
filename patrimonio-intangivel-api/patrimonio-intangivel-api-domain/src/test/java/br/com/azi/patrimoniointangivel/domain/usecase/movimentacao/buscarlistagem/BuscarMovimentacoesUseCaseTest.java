package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarlistagem;

import br.com.azi.patrimoniointangivel.domain.entity.ListaPaginada;
import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarlistagem.converter.BuscarMovimentacaoFiltroConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarlistagem.converter.BuscarMovimentacoesOutputDataConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BuscarMovimentacoesUseCaseTest {

    @InjectMocks
    private BuscarMovimentacoesUseCaseImpl useCase;

    @Mock
    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @InjectMocks
    private BuscarMovimentacoesOutputDataConverter outputDataConverter;

    @InjectMocks
    private BuscarMovimentacaoFiltroConverter buscarMovimentacaoFiltroConverter;

    @Before
    public void gerarInstanciaBuscarMovimentacaoUseCase(){
       useCase = new BuscarMovimentacoesUseCaseImpl(buscarMovimentacaoFiltroConverter, sistemaDeGestaoAdministrativaIntegration, movimentacaoDataProvider, outputDataConverter);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalaharCasoOsParametrosNaoForemPassados(){
        useCase.executar(new BuscarMovimentacoesInputData());
    }

    @Test
    public void deveBuscarListagemComFiltrosCorretos(){
        BuscarMovimentacoesInputData inputData = new BuscarMovimentacoesInputData();
        inputData.setPage(2L);
        inputData.setSize(10L);
        inputData.setSort("ASC");

        Mockito.when(sistemaDeGestaoAdministrativaIntegration.buscarUnidadesOrganizacionaisPorFuncao(any(List.class)))
            .thenReturn(
                List.of(
                    UnidadeOrganizacional.builder().id(1L).build()
                )
            );

        Mockito.when(movimentacaoDataProvider.buscarPorFiltro(any(Movimentacao.Filtro.class)))
            .thenReturn(
                ListaPaginada.<Movimentacao>builder()
                    .items(
                        List.of(Movimentacao.builder()
                            .id(1L)
                            .codigo("00001")
                            .situacao(Movimentacao.Situacao.AGUARDANDO_RECEBIMENTO)
                            .dataCadastro(LocalDateTime.of(2020, Month.AUGUST,8,12,54))
                            .orgaoOrigem(UnidadeOrganizacional.builder().id(2L).descricao("Agencia nacional de Transito").sigla("DETRAN").build())
                            .orgaoDestino(UnidadeOrganizacional.builder().id(2L).descricao("Agência Estadual de Regulação de Serviços Públicos de Mato Grosso do Sul").sigla("AGEPAN").build())
                            .build())
                    )
                    .totalElements(1L)
                    .totalPages(2L)
                    .build());

        BuscarMovimentacoesOutputData outputData = useCase.executar(inputData);

        Assert.assertTrue(outputData.items instanceof ArrayList);
        Assert.assertFalse(outputData.items.isEmpty());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.items.get(0).getId());
        Assert.assertEquals(Long.valueOf(1L),outputData.getTotalElements());
        Assert.assertEquals(Long.valueOf(2L),outputData.getTotalPages());
        Assert.assertEquals(Long.valueOf(1), outputData.items.get(0).getId());
        Assert.assertEquals("00001", outputData.items.get(0).getCodigo());
        Assert.assertEquals("DETRAN", outputData.items.get(0).getOrgaoOrigem());
        Assert.assertEquals("AGEPAN", outputData.items.get(0).getOrgaoDestino());
    }
}
