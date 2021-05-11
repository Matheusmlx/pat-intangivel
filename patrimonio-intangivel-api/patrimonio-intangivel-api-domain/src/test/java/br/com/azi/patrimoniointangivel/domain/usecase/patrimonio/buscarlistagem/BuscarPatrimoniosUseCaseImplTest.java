package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarlistagem;

import br.com.azi.patrimoniointangivel.domain.entity.ListaPaginada;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarlistagem.converter.BuscarPatrimoniosFiltroConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarlistagem.converter.BuscarPatrimoniosOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarlistagem.exception.FiltroIncompletoException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class BuscarPatrimoniosUseCaseImplTest {

    @Test
    public void deveFalharQuandoNaoTemFiltro() {
        BuscarPatrimoniosInputData buscarPatrimonioInputData = new BuscarPatrimoniosInputData();
        buscarPatrimonioInputData.setSort("ASC");

        try {
            new BuscarPatrimoniosUseCaseImpl(null,  null, null, null).executar(buscarPatrimonioInputData);
        } catch (Exception e) {
            Assert.assertEquals(2, e.getSuppressed().length);
            Assert.assertTrue(e.getSuppressed()[0] instanceof FiltroIncompletoException);
        }
    }

    @Test
    public void deveBuscarListagemComFiltrosCorretos() {
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration = Mockito.mock(SistemaDeGestaoAdministrativaIntegration.class);

        BuscarPatrimoniosUseCaseImpl useCase = new BuscarPatrimoniosUseCaseImpl(
            patrimonioDataProvider,
            sistemaDeGestaoAdministrativaIntegration,
            new BuscarPatrimoniosFiltroConverter(),
            new BuscarPatrimoniosOutputDataConverter()
        );


        BuscarPatrimoniosInputData inputData = new BuscarPatrimoniosInputData();
        inputData.setPage(0L);
        inputData.setSize(10L);
        inputData.setSort("ASC");

        Mockito.when(sistemaDeGestaoAdministrativaIntegration.buscarUnidadesOrganizacionaisPorFuncao(any(List.class)))
            .thenReturn(
                List.of(
                    UnidadeOrganizacional.builder().id(1L).build()
                )
            );

        Mockito.when(patrimonioDataProvider.buscarPorFiltro(any(Patrimonio.Filtro.class)))
            .thenReturn(
                ListaPaginada.<Patrimonio>builder()
                    .items(
                        List.of(Patrimonio.builder()
                            .id(1L)
                            .nome("Software intagivel")
                            .tipo(Patrimonio.Tipo.SOFTWARES)
                            .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                            .build())
                    ).build());

        BuscarPatrimoniosOutputData outputData = useCase.executar(inputData);

        Assert.assertTrue(outputData.items instanceof ArrayList);
        Assert.assertFalse(outputData.items.isEmpty());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.items.get(0).getId());
        Assert.assertEquals("SOFTWARES", outputData.items.get(0).getTipo());
    }

    @Test
    public void deveBuscarListagemSemFiltro() {
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration = Mockito.mock(SistemaDeGestaoAdministrativaIntegration.class);

        BuscarPatrimoniosUseCaseImpl useCase = new BuscarPatrimoniosUseCaseImpl(
            patrimonioDataProvider,
            sistemaDeGestaoAdministrativaIntegration,
            new BuscarPatrimoniosFiltroConverter(),
            new BuscarPatrimoniosOutputDataConverter()
        );


        BuscarPatrimoniosInputData inputData = new BuscarPatrimoniosInputData();
        inputData.setPage(0L);
        inputData.setSize(10L);
        inputData.setSort("ASC");

        Mockito.when(sistemaDeGestaoAdministrativaIntegration.buscarUnidadesOrganizacionaisPorFuncao(any(List.class)))
            .thenReturn(Collections.emptyList());

        Mockito.when(patrimonioDataProvider.buscarPorFiltro(any(Patrimonio.Filtro.class)))
            .thenReturn(
                ListaPaginada.<Patrimonio>builder()
                    .items(
                        List.of(Patrimonio.builder()
                            .id(1L)
                            .nome("Software intagivel")
                            .tipo(Patrimonio.Tipo.SOFTWARES)
                            .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                            .build())
                    ).build());

        BuscarPatrimoniosOutputData outputData = useCase.executar(inputData);

        Assert.assertTrue(outputData.items instanceof ArrayList);
    }

    @Test
    public void deveRetornarListagemOrdenadaPorSituacao() {
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration = Mockito.mock(SistemaDeGestaoAdministrativaIntegration.class);

        BuscarPatrimoniosUseCaseImpl useCase = new BuscarPatrimoniosUseCaseImpl(
            patrimonioDataProvider,
            sistemaDeGestaoAdministrativaIntegration,
            new BuscarPatrimoniosFiltroConverter(),
            new BuscarPatrimoniosOutputDataConverter());

        BuscarPatrimoniosInputData inputData = new BuscarPatrimoniosInputData();
        inputData.setPage(0L);
        inputData.setSize(10L);
        inputData.setSort("situacao desc");

        Mockito.when(sistemaDeGestaoAdministrativaIntegration.buscarUnidadesOrganizacionaisPorFuncao(any(List.class)))
            .thenReturn(
                List.of(
                    UnidadeOrganizacional.builder().id(1L).build()
                )
            );

        Mockito.when(patrimonioDataProvider.buscarPorFiltro(any(Patrimonio.Filtro.class)))
            .thenReturn(
                ListaPaginada.<Patrimonio>builder()
                    .items(
                        List.of(
                            Patrimonio.builder()
                                .id(1L)
                                .nome("Software intagivel")
                                .tipo(Patrimonio.Tipo.SOFTWARES)
                                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                                .build(),
                            Patrimonio.builder()
                                .id(1L)
                                .nome("Licenca intagivel")
                                .tipo(Patrimonio.Tipo.LICENCAS)
                                .situacao(Patrimonio.Situacao.ATIVO)
                                .build()
                        )
                    ).build());

        BuscarPatrimoniosOutputData outputData = useCase.executar(inputData);

        Assert.assertTrue(outputData.items instanceof ArrayList);
        Assert.assertFalse(outputData.items.isEmpty());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.items.get(0).getId());
        Assert.assertEquals("EM_ELABORACAO", outputData.items.get(0).getSituacao());
    }
}
