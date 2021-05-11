package br.com.azi.patrimoniointangivel.domain.usecase.unidadeorganizacional;

import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.unidadeorganizacional.buscarfilhos.BuscarUnidadeOrganizacionalPorIdOrgaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.unidadeorganizacional.buscarfilhos.BuscarUnidadeOrganizacionalPorIdOrgaoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.unidadeorganizacional.buscarfilhos.BuscarUnidadeOrganizacionalPorIdOrgaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.unidadeorganizacional.buscarfilhos.converter.BuscarUnidadeOrganizacionalPorIdOutputDataConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BuscarUnidadeOrganizacionalPorIdOrgaoUseCaseTest {

    @Test
    public void deveRetornarUnidadesFilhas() {
        SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration = Mockito.mock(SistemaDeGestaoAdministrativaIntegration.class);

        BuscarUnidadeOrganizacionalPorIdOrgaoInputData inputData = new BuscarUnidadeOrganizacionalPorIdOrgaoInputData();
        inputData.setId(1L);

        Mockito.when(sistemaDeGestaoAdministrativaIntegration.buscarUnidadeOrganizacionalPorId(any(Long.class)))
            .thenReturn(List.of(
                UnidadeOrganizacional.builder()
                    .id(1L)
                    .nome("Agência de Regulação dos Serviços Públicos Delegados de Campo Grande")
                    .sigla("AGEREG")
                    .situacao("ATIVO")
                    .codHierarquia("0000.0001")
                    .build(),
                UnidadeOrganizacional.builder()
                    .id(2L)
                    .nome("Agência Municipal de Habitação")
                    .sigla("EMHA")
                    .situacao("INATIVO")
                    .codHierarquia("0000.0002")
                    .descricao("Descrição agência Municipal")
                    .build()));

        BuscarUnidadeOrganizacionalPorIdOrgaoUseCaseImpl usecase = new BuscarUnidadeOrganizacionalPorIdOrgaoUseCaseImpl(
            sistemaDeGestaoAdministrativaIntegration,
            new BuscarUnidadeOrganizacionalPorIdOutputDataConverter());

        BuscarUnidadeOrganizacionalPorIdOrgaoOutputData outputData = usecase.executar(inputData);
        Mockito.verify(sistemaDeGestaoAdministrativaIntegration,
            Mockito.times(1)).buscarUnidadeOrganizacionalPorId(1L);
        Assert.assertEquals(Long.valueOf(2), outputData.getItems().get(1).getId());
        Assert.assertEquals("Agência Municipal de Habitação", outputData.getItems().get(1).getNome());
        Assert.assertEquals("EMHA", outputData.getItems().get(1).getSigla());
        Assert.assertEquals("0000.0002", outputData.getItems().get(1).getCodHierarquia());
        Assert.assertEquals("Descrição agência Municipal", outputData.getItems().get(1).getDescricao());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoPassarId() {
        SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration = Mockito.mock(SistemaDeGestaoAdministrativaIntegration.class);
        BuscarUnidadeOrganizacionalPorIdOutputDataConverter converter = Mockito.mock(BuscarUnidadeOrganizacionalPorIdOutputDataConverter.class);

        BuscarUnidadeOrganizacionalPorIdOrgaoUseCaseImpl usecase = new BuscarUnidadeOrganizacionalPorIdOrgaoUseCaseImpl(
            sistemaDeGestaoAdministrativaIntegration,
            converter
        );

        usecase.executar(new BuscarUnidadeOrganizacionalPorIdOrgaoInputData());
    }
}
