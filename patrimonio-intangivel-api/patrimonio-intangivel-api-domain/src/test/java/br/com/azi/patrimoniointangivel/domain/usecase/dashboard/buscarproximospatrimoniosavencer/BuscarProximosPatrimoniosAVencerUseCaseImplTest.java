package br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscarproximospatrimoniosavencer;

import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscarproximospatrimoniosavencer.converter.BuscarProximosPatrimoniosAVencerOutputDataConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BuscarProximosPatrimoniosAVencerUseCaseImplTest {

    private final static LocalDate LOCAL_DATE = LocalDate.of(2020, 12, 21);
    private final Clock fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    @InjectMocks
    private BuscarProximosPatrimoniosAVencerOutputDataConverter buscarProximosPatrimoniosAVencerOutputDataConverter;


    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoPassarDadosEntrada() {
        BuscarProximosPatrimoniosAVencerUseCaseImpl usecase = new BuscarProximosPatrimoniosAVencerUseCaseImpl(
            patrimonioDataProvider,
            sistemaDeGestaoAdministrativaIntegration,
            fixedClock,
            buscarProximosPatrimoniosAVencerOutputDataConverter);

        usecase.executar(new BuscarProximosPatrimoniosAVencerInputData());
    }

    @Test
    public void deveBuscarProximosPatrimoniosAVencer() {
        Mockito.when(sistemaDeGestaoAdministrativaIntegration.buscarUnidadesOrganizacionaisPorFuncao(any(List.class)))
            .thenReturn(List.of(
                UnidadeOrganizacional.builder()
                    .id(1L)
                    .build()));

        Mockito.when(patrimonioDataProvider.buscarProximosPatrimoniosAVencerNosOrgaos(any(List.class), any(Long.class))).thenReturn(
            List.of(
                Patrimonio.builder()
                    .id(1L)
                    .nome("Nome")
                    .fimVidaUtil(LocalDateTime.of(2024, 12, 21, 23, 59, 59))
                    .build(),
                Patrimonio.builder()
                    .id(2L)
                    .nome("Nome2")
                    .fimVidaUtil(LocalDateTime.of(2026, 12, 25, 23, 59, 59))
                    .build()
            )
        );

        BuscarProximosPatrimoniosAVencerUseCaseImpl usecase = new BuscarProximosPatrimoniosAVencerUseCaseImpl(
            patrimonioDataProvider,
            sistemaDeGestaoAdministrativaIntegration,
            fixedClock,
            buscarProximosPatrimoniosAVencerOutputDataConverter);

        BuscarProximosPatrimoniosAVencerOutputData outputData = usecase.executar(new BuscarProximosPatrimoniosAVencerInputData(3L));

        Assert.assertEquals(Long.valueOf(1), outputData.getItens().get(0).getId());
        Assert.assertEquals("Nome", outputData.getItens().get(0).getNome());
        Assert.assertEquals(Long.valueOf(1461), outputData.getItens().get(0).getDiasParaVencer());

        Assert.assertEquals(Long.valueOf(2), outputData.getItens().get(1).getId());
        Assert.assertEquals("Nome2", outputData.getItens().get(1).getNome());
        Assert.assertEquals(Long.valueOf(2195), outputData.getItens().get(1).getDiasParaVencer());
    }
}
