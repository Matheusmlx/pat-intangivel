package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao;

import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.entity.Usuario;
import br.com.azi.patrimoniointangivel.domain.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.UsuarioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.rejeitar.RejeitarMovimentacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.rejeitar.RejeitarMovimentacaoUseCaseImpl;
import org.junit.Before;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class RejeitarMovimentacaoUseCaseTest {

    private final static LocalDate LOCAL_DATE = LocalDate.of(2020, 10, 1);
    private Clock fixedClock;

    @InjectMocks
    RejeitarMovimentacaoUseCaseImpl useCase;

    @Mock
    MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    UsuarioDataProvider usuarioDataProvider;

    @Mock
    SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Before
    public void start() {
        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        useCase = new RejeitarMovimentacaoUseCaseImpl(movimentacaoDataProvider,usuarioDataProvider, sessaoUsuarioDataProvider, fixedClock);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoPassarIdMovimentacao (){
        useCase.executar(new RejeitarMovimentacaoInputData());
    }

    @Test
    public void deveRejeitarMovimentacao() {
        MovimentacaoDataProvider movimentacaoDataProvider = Mockito.mock(MovimentacaoDataProvider.class);

        RejeitarMovimentacaoInputData inputData = RejeitarMovimentacaoInputData
            .builder()
            .id(1L)
            .build();

        RejeitarMovimentacaoUseCaseImpl useCase = new RejeitarMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            usuarioDataProvider,
            sessaoUsuarioDataProvider,
            fixedClock);

        Mockito.when(movimentacaoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Movimentacao.builder()
                .id(1L)
                .motivo("Patrimonio esta sendo utilizados")
                .orgaoOrigem(UnidadeOrganizacional.builder().id(1L).build())
                .orgaoDestino(UnidadeOrganizacional.builder().id(2L).build())
                .build()));

        Mockito.when(movimentacaoDataProvider.salvar(any(Movimentacao.class)))
            .thenReturn(Movimentacao.builder()
                .id(1L)
                .motivo("Patrimonio não esta sendo mais utilizado")
                .orgaoOrigem(UnidadeOrganizacional.builder().id(1L).build())
                .orgaoDestino(UnidadeOrganizacional.builder().id(2L).build())
                .dataDeEnvio(LocalDateTime.of(2020, 10, 1, 13, 0))
                .dataDeFinalizacao(LocalDateTime.now(fixedClock))
                .build());

        Mockito.when(usuarioDataProvider.buscarUsuarioPorSessao(any(SessaoUsuario.class)))
            .thenReturn(Usuario
                .builder()
                .id(1L)
                .nome("admin")
                .build());

        Mockito.when(sessaoUsuarioDataProvider.get())
            .thenReturn(SessaoUsuario
                .builder()
                .id(1L)
                .login("admin")
                .build());

        useCase.executar(inputData);

        Mockito.verify(movimentacaoDataProvider, Mockito.times(1)).salvar(any(Movimentacao.class));
        Mockito.verify(usuarioDataProvider, Mockito.times(1)).buscarUsuarioPorSessao(any(SessaoUsuario.class));
        Mockito.verify(sessaoUsuarioDataProvider, Mockito.times(1)).get();
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveFalharCasoMovimentacaoNaoSejaEncontrada(){
        MovimentacaoDataProvider movimentacaoDataProvider = Mockito.mock(MovimentacaoDataProvider.class);

        RejeitarMovimentacaoInputData inputData = RejeitarMovimentacaoInputData
            .builder()
            .id(1L)
            .build();

        RejeitarMovimentacaoUseCaseImpl useCase = new RejeitarMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            usuarioDataProvider,
            sessaoUsuarioDataProvider,
            fixedClock);

        Mockito.when(movimentacaoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.FALSE);

        useCase.executar(inputData);

        Mockito.verify(movimentacaoDataProvider, Mockito.times(1)).existe(any(Long.class));
    }
}
