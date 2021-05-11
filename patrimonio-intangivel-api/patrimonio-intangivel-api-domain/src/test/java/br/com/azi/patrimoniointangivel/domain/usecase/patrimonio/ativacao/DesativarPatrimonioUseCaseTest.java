package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao;

import br.com.azi.patrimoniointangivel.domain.entity.ConfigAmortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.DadosAmortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.LancamentosContabeis;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.AmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigAmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DadosAmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.LancamentosContabeisDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.desativar.DesativarPatrimonioInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.desativar.DesativarPatrimonioOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.desativar.DesativarPatrimonioUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.desativar.converter.DesativarPatrimonioOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.desativar.exception.DesativarPatrimonioAmortizadoException;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.desativar.exception.DesativarPatrimonioException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DesativarPatrimonioUseCaseTest {
    @InjectMocks
    DesativarPatrimonioUseCaseImpl useCase;

    @InjectMocks
    DesativarPatrimonioOutputDataConverter outputDataConverter;

    @Mock
     PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    DadosAmortizacaoDataProvider dadosAmortizacaoDataProvider;

    @Mock
    ConfigAmortizacaoDataProvider configAmortizacaoDataProvider;

    @Mock
    AmortizacaoDataProvider amortizacaoDataProvider;

    @Mock
    LancamentosContabeisDataProvider lancamentosContabeisDataProvider;

    @Before
    public void inicializa() {
        useCase = new DesativarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            outputDataConverter,
            dadosAmortizacaoDataProvider,
            configAmortizacaoDataProvider,
            amortizacaoDataProvider,
            lancamentosContabeisDataProvider);
    }

    @Test
    public void deveDesativarPatrimonio() {
        Mockito.when(patrimonioDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.doNothing().when(configAmortizacaoDataProvider).remover(any(Long.class));
        Mockito.doNothing().when(dadosAmortizacaoDataProvider).remover(any(Long.class));

        Patrimonio patrimonioMock = Patrimonio.builder()
            .id(1L)
            .nome("Sistema Interno")
            .tipo(Patrimonio.Tipo.SOFTWARES)
            .situacao(Patrimonio.Situacao.ATIVO)
            .estado(Patrimonio.Estado.PRONTO_PARA_USO)
            .reconhecimento(Patrimonio.Reconhecimento.TRANSACAO_SEM_CONTRAPRESTACAO)
            .descricao("Software desenvolvido internamente sendo ativado")
            .valorAquisicao(BigDecimal.valueOf(10000.00))
            .vidaIndefinida(Boolean.FALSE)
            .dadosAmortizacao(DadosAmortizacao
                .builder()
                .id(1L)
                .configAmortizacao(ConfigAmortizacao
                    .builder()
                    .id(1L)
                    .build())
                .build())
            .dataVencimento(LocalDateTime
                .now()
                .withDayOfMonth(7)
                .withMonth(Month.SEPTEMBER.getValue()))
            .orgao(UnidadeOrganizacional
                .builder()
                .sigla("AGEHAB")
                .build()
            )
            .setor(UnidadeOrganizacional
                .builder()
                .sigla("setor a")
                .build()
            )
            .contaContabil(ContaContabil
                .builder()
                .id(1L)
                .codigo("124110100")
                .descricao("Conta contábil para softwares")
                .build())
            .dataAtivacao(LocalDateTime.now())
            .dataFinalAtivacao(LocalDateTime.now())
            .numero("0000000001")
            .build();

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(patrimonioMock));

        Mockito.when(patrimonioDataProvider.atualizar(any(Patrimonio.class)))
            .thenReturn(Patrimonio.builder()
                .id(1L)
                .nome("Sistema Interno")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .descricao("Software desenvolvido internamente sendo desativado")
                .valorAquisicao(BigDecimal.valueOf(10000.00))
                .valorLiquido(BigDecimal.valueOf(10000.00))
                .vidaIndefinida(Boolean.FALSE)
                .orgao(UnidadeOrganizacional
                    .builder()
                    .sigla("AGEHAB")
                    .build()
                )
                .setor(UnidadeOrganizacional
                    .builder()
                    .sigla("setor a")
                    .build()
                )
                .contaContabil(ContaContabil
                    .builder()
                    .id(1L)
                    .codigo("124110200")
                    .descricao("Conta contábil para softwares em desenvolvimento")
                    .build())
                .dataAtivacao(LocalDateTime.now())
                .numero("0000000001")
                .build());

        DesativarPatrimonioOutputData outputData = useCase.executar(new DesativarPatrimonioInputData(1L));
        assertEquals(Long.valueOf(1), outputData.getId());
    }

    @Test(expected = PatrimonioNaoEncontradoException.class)
    public void deveFalharQuandoPatrimonioNaoExiste() {
        Mockito.when(patrimonioDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.FALSE);

        useCase.executar(new DesativarPatrimonioInputData(1L));
    }

    @Test(expected = DesativarPatrimonioAmortizadoException.class)
    public void deveFalharQuandoPatrimonioJaAmortizou() {
        Mockito.when(patrimonioDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("Sistema Interno")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.ATIVO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .reconhecimento(Patrimonio.Reconhecimento.TRANSACAO_SEM_CONTRAPRESTACAO)
                .descricao("Software desenvolvido internamente sendo ativado")
                .valorAquisicao(BigDecimal.valueOf(10000.00))
                .vidaIndefinida(Boolean.FALSE)
                .dadosAmortizacao(DadosAmortizacao
                    .builder()
                    .id(1L)
                    .configAmortizacao(ConfigAmortizacao
                        .builder()
                        .id(1L)
                        .build())
                    .build())
                .dataVencimento(LocalDateTime
                    .now()
                    .withDayOfMonth(7)
                    .withMonth(Month.SEPTEMBER.getValue()))
                .orgao(UnidadeOrganizacional
                    .builder()
                    .sigla("AGEHAB")
                    .build()
                )
                .setor(UnidadeOrganizacional
                    .builder()
                    .sigla("setor a")
                    .build()
                )
                .contaContabil(ContaContabil
                    .builder()
                    .id(1L)
                    .codigo("124110100")
                    .descricao("Conta contábil para softwares")
                    .build())
                .dataAtivacao(LocalDateTime.now())
                .dataFinalAtivacao(LocalDateTime.now())
                .numero("0000000001")
                .build()));

        Mockito.when(amortizacaoDataProvider.existePorPatrimonio(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        useCase.executar(new DesativarPatrimonioInputData(1L));
        verify(lancamentosContabeisDataProvider,times(1)).salvar(any(LancamentosContabeis.class));
    }

    @Test(expected = DesativarPatrimonioException.class)
    public void deveFalharQuandoNaoMesCorrente() {
        Mockito.when(patrimonioDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("Sistema Interno")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.ATIVO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .reconhecimento(Patrimonio.Reconhecimento.TRANSACAO_SEM_CONTRAPRESTACAO)
                .descricao("Software desenvolvido internamente sendo ativado")
                .valorAquisicao(BigDecimal.valueOf(10000.00))
                .vidaIndefinida(Boolean.FALSE)
                .dadosAmortizacao(DadosAmortizacao
                    .builder()
                    .id(1L)
                    .configAmortizacao(ConfigAmortizacao
                        .builder()
                        .id(1L)
                        .build())
                    .build())
                .dataVencimento(LocalDateTime
                    .now()
                    .withDayOfMonth(7)
                    .withMonth(Month.SEPTEMBER.getValue()))
                .orgao(UnidadeOrganizacional
                    .builder()
                    .sigla("AGEHAB")
                    .build()
                )
                .setor(UnidadeOrganizacional
                    .builder()
                    .sigla("setor a")
                    .build()
                )
                .contaContabil(ContaContabil
                    .builder()
                    .id(1L)
                    .codigo("124110100")
                    .descricao("Conta contábil para softwares")
                    .build())
                .dataAtivacao(LocalDateTime
                    .now()
                    .withYear(2019)
                    .withDayOfMonth(7)
                    .withMonth(Month.SEPTEMBER.getValue()))
                .dataFinalAtivacao(LocalDateTime
                    .now()
                    .withYear(2019)
                    .withDayOfMonth(7)
                    .withMonth(Month.SEPTEMBER.getValue()))
                .numero("0000000001")
                .build()));

        useCase.executar(new DesativarPatrimonioInputData(1L));

    }
}
