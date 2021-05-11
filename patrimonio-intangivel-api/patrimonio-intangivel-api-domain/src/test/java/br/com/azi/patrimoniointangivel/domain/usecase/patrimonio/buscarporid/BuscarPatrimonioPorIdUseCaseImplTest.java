package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarporid;

import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.exception.NaoPossuiAcessoAoOrgaoVinculadoException;
import br.com.azi.patrimoniointangivel.domain.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarporid.converter.BuscarPatrimonioPorIdOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.edicao.exception.EditarPatrimonioAtivoException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BuscarPatrimonioPorIdUseCaseImplTest {

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoBuscaSemId() {
        BuscarPatrimonioPorIdUseCaseImpl useCase = new BuscarPatrimonioPorIdUseCaseImpl(
            Mockito.mock(PatrimonioDataProvider.class),
            Mockito.mock(SistemaDeGestaoAdministrativaIntegration.class),
            new BuscarPatrimonioPorIdOutputDataConverter());

        BuscarPatrimonioPorIdInputData inputData = new BuscarPatrimonioPorIdInputData();
        useCase.executar(inputData);
    }

    @Test(expected = PatrimonioNaoEncontradoException.class)
    public void deveFalharQuandoPatrimonioNaoExistir() {
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration = Mockito.mock(SistemaDeGestaoAdministrativaIntegration.class);

        BuscarPatrimonioPorIdUseCaseImpl useCase = new BuscarPatrimonioPorIdUseCaseImpl(
            patrimonioDataProvider,
            sistemaDeGestaoAdministrativaIntegration,
            new BuscarPatrimonioPorIdOutputDataConverter()
        );

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        BuscarPatrimonioPorIdInputData inputData = new BuscarPatrimonioPorIdInputData(1L);
        useCase.executar(inputData);
    }

    @Test
    public void deveBuscarPatrimonio() {
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration = Mockito.mock(SistemaDeGestaoAdministrativaIntegration.class);

        BuscarPatrimonioPorIdUseCaseImpl useCase = new BuscarPatrimonioPorIdUseCaseImpl(
            patrimonioDataProvider,
            sistemaDeGestaoAdministrativaIntegration,
            new BuscarPatrimonioPorIdOutputDataConverter()
        );

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                Patrimonio
                    .builder()
                    .id(1L)
                    .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                    .nome("Software do bom")
                    .valorAquisicao(BigDecimal.valueOf(100))
                    .descricao("Software muito bom feito por um cara bom")
                    .build()
            ));

        BuscarPatrimonioPorIdInputData inputData = new BuscarPatrimonioPorIdInputData(1L);
        BuscarPatrimonioPorIdOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(outputData.getId(), Long.valueOf(1));
        Assert.assertEquals(outputData.getNome(), "Software do bom");
        Assert.assertEquals(outputData.getDescricao(), "Software muito bom feito por um cara bom");
    }


    @Test(expected = EditarPatrimonioAtivoException.class)
    public void deveFalharQuandoPatrimonioAtivo() {
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration = Mockito.mock(SistemaDeGestaoAdministrativaIntegration.class);

        BuscarPatrimonioPorIdUseCaseImpl useCase = new BuscarPatrimonioPorIdUseCaseImpl(
            patrimonioDataProvider,
            sistemaDeGestaoAdministrativaIntegration,
            new BuscarPatrimonioPorIdOutputDataConverter()
        );

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                Patrimonio
                    .builder()
                    .id(1L)
                    .situacao(Patrimonio.Situacao.ATIVO)
                    .nome("Software do bom")
                    .valorAquisicao(BigDecimal.valueOf(100))
                    .descricao("Software muito bom feito por um cara bom")
                    .build()
            ));

        BuscarPatrimonioPorIdInputData inputData = new BuscarPatrimonioPorIdInputData(1L);
        useCase.executar(inputData);
    }

    @Test(expected = NaoPossuiAcessoAoOrgaoVinculadoException.class)
    public void deveFalharQuandoNaoPossuirAcessoAoOrgao() {
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration = Mockito.mock(SistemaDeGestaoAdministrativaIntegration.class);

        BuscarPatrimonioPorIdUseCaseImpl useCase = new BuscarPatrimonioPorIdUseCaseImpl(
            patrimonioDataProvider,
            sistemaDeGestaoAdministrativaIntegration,
            new BuscarPatrimonioPorIdOutputDataConverter()
        );

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                Patrimonio
                    .builder()
                    .id(1L)
                    .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                    .nome("Software do bom")
                    .valorAquisicao(BigDecimal.valueOf(100))
                    .orgao(UnidadeOrganizacional.builder() .id(1L) .build())
                    .descricao("Software muito bom feito por um cara bom")
                    .build()
            ));

        Mockito.when(sistemaDeGestaoAdministrativaIntegration.verificarDominioUnidadeOrganizacionalPorIdEFuncao(any(Long.class), any()))
            .thenReturn(Boolean.FALSE);

        BuscarPatrimonioPorIdInputData inputData = new BuscarPatrimonioPorIdInputData(1L);
        useCase.executar(inputData);
    }
}
