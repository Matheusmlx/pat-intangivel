package br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscartotalizadoresportipo;

import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscartotalizadoresportipo.converter.BuscarTotalizadoresPorTipoOutputDataConverter;
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
public class BuscarTotalizadoresPorTipoUseCaseImplTest {

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    @InjectMocks
    private BuscarTotalizadoresPorTipoOutputDataConverter buscarTotalizadoresPorTipoOutputDataConverter;

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

        Mockito.when(patrimonioDataProvider.contarPorTipoEOrgaos(any(String.class), any(List.class))).thenReturn(0L);

        BuscarTotalizadoresPorTipoUseCaseImpl usecase = new BuscarTotalizadoresPorTipoUseCaseImpl(
            patrimonioDataProvider,
            sistemaDeGestaoAdministrativaIntegration,
            buscarTotalizadoresPorTipoOutputDataConverter);

        BuscarTotalizadoresPorTipoOutputData outputData = usecase.executar();

        Assert.assertEquals(6, outputData.getItens().size());
        Assert.assertEquals("Softwares", outputData.getItens().get(0).getNome());
        Assert.assertEquals("SOFTWARES", outputData.getItens().get(0).getTipo());
        Assert.assertEquals(Long.valueOf(0), outputData.getItens().get(0).getQuantidade());

        Assert.assertEquals("Direitos Autorais", outputData.getItens().get(1).getNome());
        Assert.assertEquals("DIREITOS_AUTORAIS", outputData.getItens().get(1).getTipo());
        Assert.assertEquals(Long.valueOf(0), outputData.getItens().get(1).getQuantidade());

        Assert.assertEquals("Licenças", outputData.getItens().get(2).getNome());
        Assert.assertEquals("LICENCAS", outputData.getItens().get(2).getTipo());
        Assert.assertEquals(Long.valueOf(0), outputData.getItens().get(2).getQuantidade());

        Assert.assertEquals("Marcas", outputData.getItens().get(3).getNome());
        Assert.assertEquals("MARCAS", outputData.getItens().get(3).getTipo());
        Assert.assertEquals(Long.valueOf(0), outputData.getItens().get(3).getQuantidade());

        Assert.assertEquals("Títulos de Publicação", outputData.getItens().get(4).getNome());
        Assert.assertEquals("TITULOS_DE_PUBLICACAO", outputData.getItens().get(4).getTipo());
        Assert.assertEquals(Long.valueOf(0), outputData.getItens().get(4).getQuantidade());

        Assert.assertEquals("Receitas, Fórmulas e Projetos", outputData.getItens().get(5).getNome());
        Assert.assertEquals("RECEITAS_FORMULAS_PROJETOS", outputData.getItens().get(5).getTipo());
        Assert.assertEquals(Long.valueOf(0), outputData.getItens().get(5).getQuantidade());

    }

}
