package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.visualizacao;

import br.com.azi.patrimoniointangivel.domain.entity.ConfigAmortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.DadosAmortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.exception.NaoPossuiAcessoAoOrgaoVinculadoException;
import br.com.azi.patrimoniointangivel.domain.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.AmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.visualizacao.converter.VisualizarPatrimonioOutputDataConverter;
import org.junit.Assert;
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

@RunWith(MockitoJUnitRunner.class)
public class VisualizarPatrimonioUseCaseTest {
    @InjectMocks
    VisualizarPatrimonioOutputDataConverter outputDataConverter;

    @InjectMocks
    VisualizarPatrimonioUseCaseImpl useCase;

    @Mock
    PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    @Mock
    AmortizacaoDataProvider amortizacaoDataProvider;

    @Mock
    MovimentacaoDataProvider movimentacaoDataProvider;

    @Before
    public void inicializar(){
        useCase = new VisualizarPatrimonioUseCaseImpl(patrimonioDataProvider,amortizacaoDataProvider,sistemaDeGestaoAdministrativaIntegration,outputDataConverter,movimentacaoDataProvider);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoBuscaSemId() {
        useCase.executar(new VisualizarPatrimonioInputData());
    }

    @Test(expected = PatrimonioNaoEncontradoException.class)
    public void deveFalharQuandoPatrimonioNaoExistir() {
        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        useCase.executar( new VisualizarPatrimonioInputData(1L));
    }

    @Test
    public void deveRetornarPatrimonio() {
        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                Patrimonio
                    .builder()
                    .id(1L)
                    .nome("Intellij Idea")
                    .tipo(Patrimonio.Tipo.SOFTWARES)
                    .situacao(Patrimonio.Situacao.ATIVO)
                    .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                    .reconhecimento(Patrimonio.Reconhecimento.GERACAO_INTERNA)
                    .descricao("O IntelliJ IDEA é um ambiente de desenvolvimento integrado escrito em Java para o desenvolvimento de software de computador.")
                    .valorAquisicao(BigDecimal.valueOf(20000.00))
                    .valorEntrada(BigDecimal.valueOf(20000.00))
                    .notaLancamentoContabil(NotaLancamentoContabil.builder().numero("5555NL555555").build())
                    .valorLiquido(BigDecimal.valueOf(10000.00))
                    .dataVencimento(LocalDateTime
                        .now()
                        .withDayOfMonth(7)
                        .withMonth(Month.SEPTEMBER.getValue()))
                    .fimVidaUtil(LocalDateTime
                        .now()
                        .withDayOfMonth(7)
                        .withMonth(Month.SEPTEMBER.getValue()))
                    .dataAtivacao(LocalDateTime
                        .now()
                        .withDayOfMonth(7)
                        .withMonth(Month.JANUARY.getValue()))
                    .dataFinalAtivacao(LocalDateTime
                        .now()
                        .withDayOfMonth(7)
                        .withMonth(Month.JANUARY.getValue()))
                    .inicioVidaUtil(LocalDateTime
                        .now()
                        .withDayOfMonth(7)
                        .withMonth(Month.JANUARY.getValue()))
                    .orgao(UnidadeOrganizacional.builder().id(1L).sigla("AGEHAB").build())
                    .setor(UnidadeOrganizacional.builder().id(1L).sigla("setor a").build())
                    .contaContabil(ContaContabil
                        .builder()
                        .id(1L)
                        .codigo("124110200")
                        .descricao("Conta contábil para softwares em desenvolvimento")
                        .build())
                    .numero("0000000001")
                    .dadosAmortizacao(
                        DadosAmortizacao
                            .builder()
                            .configAmortizacao(
                                ConfigAmortizacao
                                    .builder()
                                    .metodo(ConfigAmortizacao.Metodo.QUOTAS_CONSTANTES)
                                    .build()
                            )
                            .build()
                    )
                    .build()
            ));

        Mockito.when(amortizacaoDataProvider.buscarValorSubtraidoPorPatrimonioId(any(Long.class)))
            .thenReturn(BigDecimal.valueOf(300));

        Mockito.when(movimentacaoDataProvider.existePorIdPatrimonio(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(sistemaDeGestaoAdministrativaIntegration.verificarDominioUnidadeOrganizacionalPorIdEFuncao(any(Long.class), any()))
            .thenReturn(Boolean.TRUE);

        VisualizarPatrimonioInputData inputData = new VisualizarPatrimonioInputData(1L);
        VisualizarPatrimonioOutputData outputData = useCase.executar(inputData);

        assertEquals(outputData.getId(), Long.valueOf(1));
        assertEquals(outputData.getNome(), "Intellij Idea");
        assertEquals(outputData.getDescricao(), "O IntelliJ IDEA é um ambiente de desenvolvimento integrado escrito em Java para o desenvolvimento de software de computador.");
        assertEquals(outputData.getConfigAmortizacao().getValorAmortizadoAcumulado(), BigDecimal.valueOf(10000.00));
        assertEquals(outputData.getConfigAmortizacao().getValorAmortizadoMensal(), BigDecimal.valueOf(300));
        assertEquals(outputData.getValorEntrada(), BigDecimal.valueOf(20000.00));
        assertEquals(outputData.getPermitirDesativacao(), Boolean.FALSE);
    }

    @Test
    public void deveRetornarPatrimonioComAmortizacao() {
        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                Patrimonio
                    .builder()
                    .id(1L)
                    .nome("Intellij Idea")
                    .tipo(Patrimonio.Tipo.SOFTWARES)
                    .situacao(Patrimonio.Situacao.ATIVO)
                    .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                    .reconhecimento(Patrimonio.Reconhecimento.GERACAO_INTERNA)
                    .descricao("O IntelliJ IDEA é um ambiente de desenvolvimento integrado escrito em Java para o desenvolvimento de software de computador.")
                    .valorAquisicao(BigDecimal.valueOf(20000.00))
                    .valorEntrada(BigDecimal.valueOf(20000.00))
                    .notaLancamentoContabil(NotaLancamentoContabil.builder().numero("5555NL555555").build())
                    .valorLiquido(BigDecimal.valueOf(10000.00))
                    .dataVencimento(LocalDateTime
                        .now()
                        .withDayOfMonth(7)
                        .withMonth(Month.SEPTEMBER.getValue()))
                    .fimVidaUtil(LocalDateTime
                        .now()
                        .withDayOfMonth(7)
                        .withMonth(Month.SEPTEMBER.getValue()))
                    .dataAtivacao(LocalDateTime
                        .now()
                        .withDayOfMonth(7)
                        .withMonth(Month.JANUARY.getValue()))
                    .dataFinalAtivacao(LocalDateTime
                        .now()
                        .withDayOfMonth(7)
                        .withMonth(Month.APRIL.getValue()))
                    .inicioVidaUtil(LocalDateTime
                        .now()
                        .withDayOfMonth(7)
                        .withMonth(Month.JANUARY.getValue()))
                    .orgao(UnidadeOrganizacional.builder().id(1L).sigla("AGEHAB").build())
                    .setor(UnidadeOrganizacional.builder().id(1L).sigla("setor a").build())
                    .contaContabil(ContaContabil
                        .builder()
                        .id(1L)
                        .codigo("124110200")
                        .descricao("Conta contábil para softwares em desenvolvimento")
                        .build())
                    .numero("0000000001")
                    .ativacaoRetroativa(true)
                    .dadosAmortizacao(
                        DadosAmortizacao
                            .builder()
                            .configAmortizacao(
                                ConfigAmortizacao
                                    .builder()
                                    .metodo(ConfigAmortizacao.Metodo.QUOTAS_CONSTANTES)
                                    .build()
                            )
                            .build()
                    )
                    .build()
            ));

        Mockito.when(amortizacaoDataProvider.buscarValorSubtraidoPorPatrimonioId(any(Long.class)))
            .thenReturn(BigDecimal.valueOf(300));

        Mockito.when(sistemaDeGestaoAdministrativaIntegration.verificarDominioUnidadeOrganizacionalPorIdEFuncao(any(Long.class), any()))
            .thenReturn(Boolean.TRUE);

        VisualizarPatrimonioInputData inputData = new VisualizarPatrimonioInputData(1L);
        VisualizarPatrimonioOutputData outputData = useCase.executar(inputData);

        assertEquals(outputData.getId(), Long.valueOf(1));
        assertEquals(outputData.getNome(), "Intellij Idea");
        assertEquals(outputData.getDescricao(), "O IntelliJ IDEA é um ambiente de desenvolvimento integrado escrito em Java para o desenvolvimento de software de computador.");
        assertEquals(outputData.getConfigAmortizacao().getValorAmortizadoAcumulado(), BigDecimal.valueOf(10000.00));
        assertEquals(outputData.getConfigAmortizacao().getValorAmortizadoMensal(), BigDecimal.valueOf(300));
        assertEquals(outputData.getValorEntrada(), BigDecimal.valueOf(20000.00));
        assertEquals(outputData.getPermitirDesativacao(), Boolean.FALSE);
    }

    @Test(expected = NaoPossuiAcessoAoOrgaoVinculadoException.class)
    public void deveFalharQuandoNaoPossuirAcessoAoOrgao() {
        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                Patrimonio
                    .builder()
                    .id(1L)
                    .nome("Intellij Idea")
                    .tipo(Patrimonio.Tipo.SOFTWARES)
                    .situacao(Patrimonio.Situacao.ATIVO)
                    .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                    .reconhecimento(Patrimonio.Reconhecimento.GERACAO_INTERNA)
                    .descricao("O IntelliJ IDEA é um ambiente de desenvolvimento integrado escrito em Java para o desenvolvimento de software de computador.")
                    .valorAquisicao(BigDecimal.valueOf(20000.00))
                    .valorEntrada(BigDecimal.valueOf(20000.00))
                    .valorLiquido(BigDecimal.valueOf(10000.00))
                    .dataVencimento(LocalDateTime
                        .now()
                        .withDayOfMonth(7)
                        .withMonth(Month.SEPTEMBER.getValue()))
                    .fimVidaUtil(LocalDateTime
                        .now()
                        .withDayOfMonth(7)
                        .withMonth(Month.SEPTEMBER.getValue()))
                    .dataAtivacao(LocalDateTime
                        .now()
                        .withDayOfMonth(7)
                        .withMonth(Month.JANUARY.getValue()))
                    .dataFinalAtivacao(LocalDateTime
                        .now()
                        .withDayOfMonth(7)
                        .withMonth(Month.JANUARY.getValue()))
                    .inicioVidaUtil(LocalDateTime
                        .now()
                        .withDayOfMonth(7)
                        .withMonth(Month.JANUARY.getValue()))
                    .orgao(UnidadeOrganizacional.builder().id(1L).sigla("AGEHAB").build())
                    .setor(UnidadeOrganizacional.builder().id(1L).sigla("setor a").build())
                    .contaContabil(ContaContabil
                        .builder()
                        .id(1L)
                        .codigo("124110200")
                        .descricao("Conta contábil para softwares em desenvolvimento")
                        .build())
                    .numero("0000000001")
                    .dadosAmortizacao(
                        DadosAmortizacao
                            .builder()
                            .configAmortizacao(
                                ConfigAmortizacao
                                    .builder()
                                    .metodo(ConfigAmortizacao.Metodo.QUOTAS_CONSTANTES)
                                    .build()
                            )
                            .build()
                    )
                    .build()
            ));

        Mockito.when(sistemaDeGestaoAdministrativaIntegration.verificarDominioUnidadeOrganizacionalPorIdEFuncao(any(Long.class), any()))
            .thenReturn(Boolean.FALSE);

        useCase.executar(new VisualizarPatrimonioInputData(1L));
    }

    @Test
    public void deveSetarParaTrueAPermisaoDesativar(){
        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                Patrimonio
                    .builder()
                    .id(1L)
                    .nome("Intellij Idea")
                    .tipo(Patrimonio.Tipo.SOFTWARES)
                    .situacao(Patrimonio.Situacao.ATIVO)
                    .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                    .reconhecimento(Patrimonio.Reconhecimento.GERACAO_INTERNA)
                    .descricao("O IntelliJ IDEA é um ambiente de desenvolvimento integrado escrito em Java para o desenvolvimento de software de computador.")
                    .valorAquisicao(BigDecimal.valueOf(20000.00))
                    .valorEntrada(BigDecimal.valueOf(20000.00))
                    .notaLancamentoContabil(NotaLancamentoContabil.builder().numero("5555NL555555").build())
                    .valorLiquido(BigDecimal.valueOf(10000.00))
                    .dataVencimento(LocalDateTime
                        .now()
                        .withDayOfMonth(7)
                        .withMonth(LocalDateTime.now().getMonth().getValue()))
                    .fimVidaUtil(LocalDateTime
                        .now()
                        .withDayOfMonth(7)
                        .withMonth(LocalDateTime.now().getMonth().getValue()))
                    .dataAtivacao(LocalDateTime
                        .now()
                        .withDayOfMonth(1)
                        .withMonth(LocalDateTime.now().getMonth().getValue()))
                    .dataFinalAtivacao(LocalDateTime
                        .now()
                        .withDayOfMonth(7)
                        .withMonth(LocalDateTime.now().getMonth().getValue()))
                    .inicioVidaUtil(LocalDateTime
                        .now()
                        .withDayOfMonth(2)
                        .withMonth(LocalDateTime.now().getMonth().getValue()))
                    .orgao(UnidadeOrganizacional.builder().id(1L).sigla("AGEHAB").build())
                    .setor(UnidadeOrganizacional.builder().id(1L).sigla("setor a").build())
                    .contaContabil(ContaContabil
                        .builder()
                        .id(1L)
                        .codigo("124110200")
                        .descricao("Conta contábil para softwares em desenvolvimento")
                        .build())
                    .numero("0000000001")
                    .dadosAmortizacao(
                        DadosAmortizacao
                            .builder()
                            .configAmortizacao(
                                ConfigAmortizacao
                                    .builder()
                                    .metodo(ConfigAmortizacao.Metodo.QUOTAS_CONSTANTES)
                                    .build()
                            )
                            .build()
                    )
                    .build()
            ));

        Mockito.when(amortizacaoDataProvider.buscarValorSubtraidoPorPatrimonioId(any(Long.class)))
            .thenReturn(BigDecimal.valueOf(300));

        Mockito.when(movimentacaoDataProvider.existePorIdPatrimonio(any(Long.class)))
            .thenReturn(Boolean.FALSE);

        Mockito.when(sistemaDeGestaoAdministrativaIntegration.verificarDominioUnidadeOrganizacionalPorIdEFuncao(any(Long.class), any()))
            .thenReturn(Boolean.TRUE);

        VisualizarPatrimonioInputData inputData = new VisualizarPatrimonioInputData(1L);
        VisualizarPatrimonioOutputData outputData = useCase.executar(inputData);

        assertEquals(outputData.getId(), Long.valueOf(1));
        assertEquals(outputData.getNome(), "Intellij Idea");
        assertEquals(outputData.getDescricao(), "O IntelliJ IDEA é um ambiente de desenvolvimento integrado escrito em Java para o desenvolvimento de software de computador.");
        assertEquals(outputData.getConfigAmortizacao().getValorAmortizadoAcumulado(), BigDecimal.valueOf(10000.00));
        assertEquals(outputData.getConfigAmortizacao().getValorAmortizadoMensal(), BigDecimal.valueOf(300));
        assertEquals(outputData.getValorEntrada(), BigDecimal.valueOf(20000.00));
        assertEquals(Boolean.TRUE, outputData.getPermitirDesativacao());
    }
}
