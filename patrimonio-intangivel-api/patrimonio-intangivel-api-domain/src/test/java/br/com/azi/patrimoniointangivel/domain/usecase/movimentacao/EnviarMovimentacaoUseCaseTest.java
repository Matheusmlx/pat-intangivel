package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao;

import br.com.azi.patrimoniointangivel.domain.entity.Amortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.AmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.enviar.EnviarMovimentacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.enviar.EnviarMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.enviar.exception.MovimentarPatrimonioComAmortizacaoPendenteException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class EnviarMovimentacaoUseCaseTest {

    private final static LocalDate LOCAL_DATE = LocalDate.of(2020, 10, 1);
    private Clock fixedClock;

    @Mock
    private AmortizacaoDataProvider amortizacaoDataProvider;

    @Mock
    MovimentacaoDataProvider movimentacaoDataProvider;

    @Before
    public void start() {
        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
    }

    @Test
    public void deveEnviarMovimentacao() {
        EnviarMovimentacaoUseCaseImpl useCase = new EnviarMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            amortizacaoDataProvider,
            fixedClock);

        Mockito.when(movimentacaoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Movimentacao.builder()
                .id(1L)
                .motivo("Patrimonio esta sendo utilizados")
                .patrimonio(Patrimonio.builder()
                    .id(2L)
                    .amortizavel(Boolean.TRUE)
                    .dataAtivacao(LocalDateTime.of(2020, 1, 1, 0, 0 ))
                    .fimVidaUtil(LocalDateTime.of(2020, 11, 25, 0, 0 ))
                    .build())
                .orgaoOrigem(UnidadeOrganizacional.builder().id(1L).build())
                .orgaoDestino(UnidadeOrganizacional.builder().id(2L).build())
                .build()));

        Mockito.when(amortizacaoDataProvider.buscarUltimaPorPatrimonio(any(Long.class)))
            .thenReturn(Optional.of(Amortizacao.builder()
            .id(1L)
                .dataFinal(LocalDateTime.of(2020, 11, 30, 0, 0 ))
            .build()));

        Mockito.when(movimentacaoDataProvider.salvar(any(Movimentacao.class)))
            .thenReturn(Movimentacao.builder()
                .id(1L)
                .motivo("Patrimônio não está sendo mais utilizado")
                .orgaoOrigem(UnidadeOrganizacional.builder().id(1L).build())
                .orgaoDestino(UnidadeOrganizacional.builder().id(2L).build())
                .dataDeEnvio(LocalDateTime.now(fixedClock))
                .build());

        useCase.executar(new EnviarMovimentacaoInputData(1L));

        Mockito.verify(movimentacaoDataProvider, Mockito.times(1)).salvar(any(Movimentacao.class));
    }

    @Test(expected = MovimentarPatrimonioComAmortizacaoPendenteException.class)
    public void deveFalharQuandoExistirAmortizacaoPendenteEPatrimonioVencido() {
        EnviarMovimentacaoUseCaseImpl useCase = new EnviarMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            amortizacaoDataProvider,
            fixedClock);

        Mockito.when(movimentacaoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Movimentacao.builder()
                .id(1L)
                .motivo("Patrimônio está vencido")
                .patrimonio(Patrimonio.builder()
                    .id(2L)
                    .amortizavel(Boolean.TRUE)
                    .dataAtivacao(LocalDateTime.of(2020, 1, 1, 0, 0 ))
                    .fimVidaUtil(LocalDateTime.of(2020, 5, 5, 0, 0 ))
                    .build())
                .orgaoOrigem(UnidadeOrganizacional.builder().id(1L).build())
                .orgaoDestino(UnidadeOrganizacional.builder().id(2L).build())
                .build()));

        Mockito.when(amortizacaoDataProvider.buscarUltimaPorPatrimonio(any(Long.class)))
            .thenReturn(Optional.of(Amortizacao.builder()
                .id(1L)
                .dataFinal(LocalDateTime.of(2020, 4, 30, 0, 0 ))
                .build()));

        useCase.executar(new EnviarMovimentacaoInputData(1L));
    }

    @Test(expected = MovimentarPatrimonioComAmortizacaoPendenteException.class)
    public void deveFalharQuandoExistirAmortizacaoPendente() {
        EnviarMovimentacaoUseCaseImpl useCase = new EnviarMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            amortizacaoDataProvider,
            fixedClock);

        Mockito.when(movimentacaoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Movimentacao.builder()
                .id(1L)
                .motivo("Patrimônio não está em uso")
                .patrimonio(Patrimonio.builder()
                    .id(2L)
                    .amortizavel(Boolean.TRUE)
                    .dataAtivacao(LocalDateTime.of(2020, 1, 1, 0, 0 ))
                    .fimVidaUtil(LocalDateTime.of(2022, 5, 5, 0, 0 ))
                    .build())
                .orgaoOrigem(UnidadeOrganizacional.builder().id(1L).build())
                .orgaoDestino(UnidadeOrganizacional.builder().id(2L).build())
                .build()));

        Mockito.when(amortizacaoDataProvider.buscarUltimaPorPatrimonio(any(Long.class)))
            .thenReturn(Optional.of(Amortizacao.builder()
                .id(1L)
                .dataFinal(LocalDateTime.of(2020, 10, 30, 0, 0 ))
                .build()));

        useCase.executar(new EnviarMovimentacaoInputData(1L));
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveFalharQuandoNaoEncontrarMovimentacao() {
        EnviarMovimentacaoUseCaseImpl useCase = new EnviarMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            amortizacaoDataProvider,
            fixedClock);

        Mockito.when(movimentacaoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.FALSE);

        useCase.executar(new EnviarMovimentacaoInputData(1L));

    }
}
