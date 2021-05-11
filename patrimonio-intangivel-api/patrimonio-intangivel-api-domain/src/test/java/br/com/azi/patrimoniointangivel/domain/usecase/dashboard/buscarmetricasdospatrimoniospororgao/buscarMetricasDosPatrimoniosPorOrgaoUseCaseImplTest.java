package br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscarmetricasdospatrimoniospororgao;

import br.com.azi.patrimoniointangivel.domain.entity.PatrimoniosAgrupados;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscarmetricasdospatrimoniospororgao.converter.BuscarMetricasDosPatrimoniosPorOrgaoDataConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class buscarMetricasDosPatrimoniosPorOrgaoUseCaseImplTest {

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    @InjectMocks
    private BuscarMetricasDosPatrimoniosPorOrgaoDataConverter buscarMetricasDosPatrimoniosPorOrgaoDataConverter;

    @Test
    public void BuscarMetricasDosPatrimoniosPorOrgaoUseCaseImplTest() {
        Mockito.when(sistemaDeGestaoAdministrativaIntegration.buscarUnidadesOrganizacionaisPorFuncao(any(List.class)))
            .thenReturn(List.of(
                UnidadeOrganizacional.builder()
                    .id(1L)
                    .nome("Nome Orgao")
                    .sigla("NO")
                    .build())
            );

        Mockito.when(patrimonioDataProvider.buscarPatrimoniosAgrupadosPorOrgaoETipo(any(List.class))).thenReturn(
            List.of(
                PatrimoniosAgrupados.builder()
                    .idOrgao(1L)
                    .Tipo("SOFTWARES")
                    .contador(2L)
                    .build(),
                PatrimoniosAgrupados.builder()
                    .idOrgao(1L)
                    .Tipo("DIREITOS_AUTORAIS")
                    .contador(3L)
                    .build()
            )
        );

        BuscarMetricasDosPatrimoniosPorOrgaoUseCaseImpl usecase = new BuscarMetricasDosPatrimoniosPorOrgaoUseCaseImpl(
            patrimonioDataProvider,
            sistemaDeGestaoAdministrativaIntegration,
            buscarMetricasDosPatrimoniosPorOrgaoDataConverter);

        BuscarMetricasDosPatrimoniosPorOrgaoOutputData outputData = usecase.executar();

        Assert.assertEquals(1, outputData.getItens().size());
        Assert.assertEquals(Long.valueOf(1), outputData.getItens().get(0).getIdOrgao());
        Assert.assertEquals("Nome Orgao", outputData.getItens().get(0).getNome());
        Assert.assertEquals("NO", outputData.getItens().get(0).getSigla());
        Assert.assertEquals(Long.valueOf(5), outputData.getItens().get(0).getTotal());
        Assert.assertEquals(2, outputData.getItens().get(0).getTipos().size());

        Assert.assertEquals("DIREITOS_AUTORAIS", outputData.getItens().get(0).getTipos().get(0).getNome());
        Assert.assertEquals(Long.valueOf(3), outputData.getItens().get(0).getTipos().get(0).getQuantidade());

        Assert.assertEquals("SOFTWARES", outputData.getItens().get(0).getTipos().get(1).getNome());
        Assert.assertEquals(Long.valueOf(2), outputData.getItens().get(0).getTipos().get(1).getQuantidade());
    }

}
