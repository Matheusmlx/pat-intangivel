package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao;

import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporid.BuscarMovimentacaoPorIdInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporid.BuscarMovimentacaoPorIdOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporid.BuscarMovimentacaoPorIdUseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporid.converter.BuscarMovimentacaoPorIdOutputDataConverter;
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
public class BuscarMovimentacaoPorIdUseCaseTest {

    @InjectMocks
    private BuscarMovimentacaoPorIdUseImpl useCase;

    @Mock
    private MovimentacaoDataProvider dataProvider;

    @InjectMocks
    private BuscarMovimentacaoPorIdOutputDataConverter outputDataConverter;

    @Before
    public void gerarInstanciaDoUseCase(){
        useCase = new BuscarMovimentacaoPorIdUseImpl(dataProvider,outputDataConverter);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdNaoForPassadoPorParametro(){

        useCase.executar(new BuscarMovimentacaoPorIdInputData());
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveFalharCasoAMovimentacaoNaoExista(){

        Mockito.when(dataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        useCase.executar(new BuscarMovimentacaoPorIdInputData(1L));
    }

    @Test
    public void deveBuscarUmaMovimentacaoPorId(){

        Mockito.when(dataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Movimentacao
                .builder()
                .id(1L)
                .situacao(Movimentacao.Situacao.AGUARDANDO_RECEBIMENTO)
                .motivo("Patrimonio não está mais sendo usado por esse orgão")
                .tipo(Movimentacao.Tipo.DOACAO_ENTRE_ORGAOS)
                .orgaoOrigem(UnidadeOrganizacional.builder().id(1L).sigla("AGEP").descricao("Agencia Estadual do Pará").build())
                .orgaoDestino(UnidadeOrganizacional.builder().id(2L).sigla("DETRAN").descricao("departamento estadual de trânsito").build())
                .build()));

        BuscarMovimentacaoPorIdOutputData outputData = useCase.executar(new BuscarMovimentacaoPorIdInputData(1L));

        assertEquals(Long.valueOf(1), outputData.getId());
        assertEquals("Patrimonio não está mais sendo usado por esse orgão", outputData.getMotivo());
        assertEquals("AGUARDANDO_RECEBIMENTO",outputData.getSituacao());
        assertEquals("DOACAO_ENTRE_ORGAOS",outputData.getTipo());
        assertEquals(Long.valueOf(1), outputData.getOrgaoOrigem().getId());
        assertEquals("AGEP", outputData.getOrgaoOrigem().getSigla());
        assertEquals("Agencia Estadual do Pará", outputData.getOrgaoOrigem().getDescricao());
        assertEquals(Long.valueOf(2), outputData.getOrgaoDestino().getId());
        assertEquals("DETRAN", outputData.getOrgaoDestino().getSigla());
        assertEquals("departamento estadual de trânsito", outputData.getOrgaoDestino().getDescricao());

    }
}
