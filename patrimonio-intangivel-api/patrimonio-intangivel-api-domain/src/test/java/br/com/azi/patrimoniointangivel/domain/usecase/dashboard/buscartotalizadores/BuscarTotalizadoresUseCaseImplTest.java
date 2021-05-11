package br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscartotalizadores;

import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscartotalizadores.converter.BuscarTotalizadoresOutputDataConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BuscarTotalizadoresUseCaseImplTest {

    private PatrimonioDataProvider patrimonioDataProvider;
    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;
    private BuscarTotalizadoresOutputDataConverter buscarTotalizadoresOutputDataConverter;

    @Before
    public void start() {
        patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        sistemaDeGestaoAdministrativaIntegration = Mockito.mock(SistemaDeGestaoAdministrativaIntegration.class);
        buscarTotalizadoresOutputDataConverter = new BuscarTotalizadoresOutputDataConverter();
    }


    @Test
    public void buscarTotalizadoresUseCaseImplTest() {
        Mockito.when(sistemaDeGestaoAdministrativaIntegration.buscarUnidadesOrganizacionaisPorFuncao(any(List.class)))
            .thenReturn(List.of(
                UnidadeOrganizacional.builder()
                    .id(1L)
                    .build())
            );

        Mockito.when(patrimonioDataProvider.contarTotalDeRegistrosPorOrgaos(any(List.class))).thenReturn(10L);
        Mockito.when(patrimonioDataProvider.contarEmElaboracaoPorOrgaos(any(List.class))).thenReturn(6L);
        Mockito.when(patrimonioDataProvider.contarAtivosPorOrgaos(any(List.class))).thenReturn(4L);


        BuscarTotalizadoresUseCaseImpl usecase = new BuscarTotalizadoresUseCaseImpl(
            patrimonioDataProvider,
            sistemaDeGestaoAdministrativaIntegration,
            buscarTotalizadoresOutputDataConverter);

        BuscarTotalizadoresOutputData outputData = usecase.executar();

        Assert.assertEquals(Long.valueOf(10), outputData.getTotalDeRegistros());
        Assert.assertEquals(Long.valueOf(6), outputData.getEmElaboracao());
        Assert.assertEquals(Long.valueOf(4), outputData.getAtivos());
    }
}
