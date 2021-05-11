package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao;

import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarPatrimonioPorMovimentacao.BuscarPatrimonioPorMovimentacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarPatrimonioPorMovimentacao.BuscarPatrimonioPorMovimentacaoMovimentacaoUseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarPatrimonioPorMovimentacao.BuscarPatrimonioPorMovimentacaoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarPatrimonioPorMovimentacao.converter.BuscarPatrimonioPorMovimentacaoOutputDataConverter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BuscarPatrimonioPorMovimentacaoUseCaseTest {

    @InjectMocks
    private BuscarPatrimonioPorMovimentacaoMovimentacaoUseImpl useCase;

    @Mock
    private PatrimonioDataProvider dataProvider;

    @InjectMocks
    private BuscarPatrimonioPorMovimentacaoOutputDataConverter outputDataConverter;

    @Before
    public void inicializar() {
        useCase = new BuscarPatrimonioPorMovimentacaoMovimentacaoUseImpl(dataProvider, outputDataConverter);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdNaoForPassadoPorParametro() {

        useCase.executar(new BuscarPatrimonioPorMovimentacaoInputData());
    }

    @Test(expected = PatrimonioNaoEncontradoException.class)
    public void deveFalharCasoOPatrimonioNaoExista() {

        Mockito.when(dataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        useCase.executar(new BuscarPatrimonioPorMovimentacaoInputData(1L));
    }

    @Test
    public void deveBuscarUmPatrimonioPorMovimentacao() {

        Mockito.when(dataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio
                .builder()
                .id(1L)
                .nome("Patrimonio Teste")
                .numero("000001")
                .setor(UnidadeOrganizacional.builder()
                    .id(1L)
                    .descricao("Agência de Regulação dos Serviços Públicos Delegados de Campo Grande")
                    .sigla("AGEREG")
                    .build())
                .build()));

        BuscarPatrimonioPorMovimentacaoOutputData outputData = useCase.executar(new BuscarPatrimonioPorMovimentacaoInputData(1L));

        assertEquals(Long.valueOf(1), outputData.getId());
        assertEquals("Patrimonio Teste", outputData.getNome());
        assertEquals("000001", outputData.getNumero());
        assertEquals("Agência de Regulação dos Serviços Públicos Delegados de Campo Grande", outputData.getSetor());

    }
}
