package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao;

import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.remover.RemoverMovimentacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.remover.RemoverMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.remover.exception.SituacaoMovimentacaoException;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

public class RemoverMovimentacaoUseCaseTest {

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoVierOIdDaMovimentacao(){
        MovimentacaoDataProvider movimentacaoDataProvider = Mockito.mock(MovimentacaoDataProvider.class);

        RemoverMovimentacaoUseCaseImpl useCase = new RemoverMovimentacaoUseCaseImpl(movimentacaoDataProvider);

        useCase.executar(new RemoverMovimentacaoInputData());
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveFalharSeMovimentacaoNaoExistir() {
        MovimentacaoDataProvider movimentacaoDataProvider = Mockito.mock(MovimentacaoDataProvider.class);

        RemoverMovimentacaoUseCaseImpl useCase = new RemoverMovimentacaoUseCaseImpl(movimentacaoDataProvider);

        Mockito.when(movimentacaoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.FALSE);

        useCase.executar(new RemoverMovimentacaoInputData(1L));
    }
    @Test(expected = SituacaoMovimentacaoException.class)
    public void deveFalharQuandoMovimentacaoNaoEstiverEmElaboracao(){
        MovimentacaoDataProvider movimentacaoDataProvider = Mockito.mock(MovimentacaoDataProvider.class);

        RemoverMovimentacaoUseCaseImpl useCase = new RemoverMovimentacaoUseCaseImpl(movimentacaoDataProvider);

        Mockito.when(movimentacaoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Movimentacao.builder()
                .id(1L)
                .motivo("Patrimonio em desuso")
                .situacao(Movimentacao.Situacao.AGUARDANDO_RECEBIMENTO)
                .build()));

        useCase.executar(new RemoverMovimentacaoInputData(1L));

    }
    @Test
    public void deveRemoverUmaMovimentacao(){
        MovimentacaoDataProvider movimentacaoDataProvider = Mockito.mock(MovimentacaoDataProvider.class);

        RemoverMovimentacaoUseCaseImpl useCase = new RemoverMovimentacaoUseCaseImpl(movimentacaoDataProvider);

        Mockito.when(movimentacaoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Movimentacao.builder()
                .id(1L)
                .motivo("Patrimonio em desuso")
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .build()));

        useCase.executar(new RemoverMovimentacaoInputData(1L));

        Mockito.verify(movimentacaoDataProvider,Mockito.times(1)).buscarPorId(any(Long.class));
        Mockito.verify(movimentacaoDataProvider,Mockito.times(1)).remover(any(Long.class));

    }
}
