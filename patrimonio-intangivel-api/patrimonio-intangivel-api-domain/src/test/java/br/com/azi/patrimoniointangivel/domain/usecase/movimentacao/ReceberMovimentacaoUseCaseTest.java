package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao;

import br.com.azi.patrimoniointangivel.domain.entity.LancamentosContabeis;
import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.entity.Usuario;
import br.com.azi.patrimoniointangivel.domain.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniointangivel.domain.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.LancamentosContabeisDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.UnidadeOrganizacionalDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.UsuarioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.receber.ReceberMovimentacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.receber.ReceberMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.receber.exception.SituacaoEmElaboracaoException;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.receber.exception.SituacaoOrgaoDestinoInativoException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReceberMovimentacaoUseCaseTest {
    @InjectMocks
    ReceberMovimentacaoUseCaseImpl useCase;

    @Mock
    MovimentacaoDataProvider movimentacaoDataProvider;
    @Mock
    PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    UnidadeOrganizacionalDataProvider unidadeOrganizacionalDataProvider;

    @Mock
    UsuarioDataProvider usuarioDataProvider;

    @Mock
    SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Mock
    LancamentosContabeisDataProvider lancamentosContabeisDataProvider;

    private final static LocalDate LOCAL_DATE = LocalDate.of(2020, 10, 1);
    private Clock fixedClock;

    @Before
    public void inicializar(){
        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        useCase = new ReceberMovimentacaoUseCaseImpl(movimentacaoDataProvider, patrimonioDataProvider, unidadeOrganizacionalDataProvider, usuarioDataProvider, sessaoUsuarioDataProvider, lancamentosContabeisDataProvider, fixedClock);
    }

    @Test(expected =IllegalStateException.class )
    public void deveFalharQuandoNaoPassarIdMovimentacao(){
        useCase.executar(new ReceberMovimentacaoInputData());
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveFalharCasoAMovimentacaoNaoSejaEncontrada(){
        when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        useCase.executar(new ReceberMovimentacaoInputData(1L));
    }

    @Test(expected = SituacaoEmElaboracaoException.class)
    public void deveFalharCasoASituacaoDaMovimentacaoEstejaEmElaboracao(){
        when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Movimentacao
                .builder()
                .id(1L)
                .tipo(Movimentacao.Tipo.DOACAO_ENTRE_ORGAOS)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .patrimonio(Patrimonio.builder().id(2L).build())
                .build()));

        useCase.executar(new ReceberMovimentacaoInputData(1L));
    }

    @Test(expected = PatrimonioNaoEncontradoException.class)
    public void deveFalharCasoOPatrimonioNaoSejaEncontrado() {
        when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Movimentacao
                .builder()
                .id(1L)
                .tipo(Movimentacao.Tipo.DOACAO_ENTRE_ORGAOS)
                .orgaoOrigem(UnidadeOrganizacional.builder().sigla("AGEPAN").build())
                .orgaoDestino(UnidadeOrganizacional.builder().id(1L).sigla("DETRAN").build())
                .situacao(Movimentacao.Situacao.AGUARDANDO_RECEBIMENTO)
                .patrimonio(Patrimonio.builder().id(2L).build())
                .build()));

        when(unidadeOrganizacionalDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(UnidadeOrganizacional
                .builder()
                .id(1L)
                .sigla("DETRAN")
                .situacao("ATIVO")
                .build()));

        when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        useCase.executar(new ReceberMovimentacaoInputData(1L));
    }

    @Test
    public void deveAtualizarOPatrimonio(){
        when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Movimentacao
                .builder()
                .id(1L)
                .tipo(Movimentacao.Tipo.DOACAO_ENTRE_ORGAOS)
                .orgaoOrigem(UnidadeOrganizacional.builder().sigla("AGEPAN").build())
                .orgaoDestino(UnidadeOrganizacional.builder().id(1L).sigla("DETRAN").build())
                .situacao(Movimentacao.Situacao.AGUARDANDO_RECEBIMENTO)
                .patrimonio(Patrimonio.builder().id(2L).build())
                .build()));

        when(unidadeOrganizacionalDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(UnidadeOrganizacional
                .builder()
                .id(1L)
                .sigla("DETRAN")
                .situacao("ATIVO")
                .build()));

        when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio
                .builder()
                 .id(2L)
                 .numero("00002")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .orgao(UnidadeOrganizacional.builder().sigla("AGEPAN").build())
                .build()));

        when(usuarioDataProvider.buscarUsuarioPorSessao(any(SessaoUsuario.class)))
            .thenReturn(Usuario
                .builder()
                .id(1L)
                .nome("admin")
                .build());

        when(sessaoUsuarioDataProvider.get())
            .thenReturn(SessaoUsuario
                .builder()
                .id(1L)
                .login("admin")
                .build());

        Patrimonio patrimonio = Patrimonio
            .builder()
            .id(2L)
            .numero("00002")
            .tipo(Patrimonio.Tipo.SOFTWARES)
            .orgao(UnidadeOrganizacional.builder().id(1L).sigla("DETRAN").build())
            .build();

        Movimentacao movimentacao = Movimentacao
            .builder()
            .id(1L)
            .tipo(Movimentacao.Tipo.DOACAO_ENTRE_ORGAOS)
            .orgaoOrigem(UnidadeOrganizacional.builder().sigla("AGEPAN").build())
            .orgaoDestino(UnidadeOrganizacional.builder().id(1L).sigla("DETRAN").build())
            .situacao(Movimentacao.Situacao.FINALIZADO)
            .dataDeFinalizacao(LocalDateTime.now(fixedClock))
            .usuarioFinalizacao("admin")
            .patrimonio(Patrimonio.builder().id(2L).build())
            .build();

        SessaoUsuario sessaoUsuario = SessaoUsuario
            .builder()
            .id(1L)
            .login("admin")
            .build();

        useCase.executar(new ReceberMovimentacaoInputData(1L));

        verify(movimentacaoDataProvider, times(1)).buscarPorId(any(Long.class));
        verify(movimentacaoDataProvider, times(1)).salvar(movimentacao);

        verify(patrimonioDataProvider, times(1)).buscarPorId(any(Long.class));
        verify(patrimonioDataProvider, times(1)).atualizar(patrimonio);

        verify(usuarioDataProvider, times(1)).buscarUsuarioPorSessao(sessaoUsuario);
        verify(sessaoUsuarioDataProvider, times(1)).get();
        verify(lancamentosContabeisDataProvider, times(2)).salvar(any(LancamentosContabeis.class));
    }

    @Test(expected = SituacaoOrgaoDestinoInativoException.class)
    public void deveFalharCasoOrgaoDestinoTenhaSidoDesativo(){
        when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Movimentacao
                .builder()
                .id(1L)
                .tipo(Movimentacao.Tipo.DOACAO_ENTRE_ORGAOS)
                .orgaoOrigem(UnidadeOrganizacional.builder().sigla("AGEPAN").build())
                .orgaoDestino(UnidadeOrganizacional.builder().id(1L).sigla("DETRAN").build())
                .situacao(Movimentacao.Situacao.AGUARDANDO_RECEBIMENTO)
                .patrimonio(Patrimonio.builder().id(2L).build())
                .build()));

        when(unidadeOrganizacionalDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(UnidadeOrganizacional
                .builder()
                .id(1L)
                .sigla("DETRAN")
                .situacao("INATIVO")
                .build()));

        useCase.executar(new ReceberMovimentacaoInputData(1L));
    }
}
