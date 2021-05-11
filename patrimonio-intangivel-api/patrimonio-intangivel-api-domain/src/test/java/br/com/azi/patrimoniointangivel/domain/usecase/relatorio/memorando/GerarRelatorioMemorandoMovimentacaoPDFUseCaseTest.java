package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando;

import br.com.azi.patrimoniointangivel.domain.entity.Amortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;
import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.LancamentosContabeis;
import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.RelatorioMemorandoMovimentacao;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.AmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.LancamentosContabeisDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.parametro.buscar.BuscarParametrosOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.parametro.buscar.BuscarParametrosUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.memorando.GerarNumeroMemorandoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.memorando.GerarNumeroMemorandoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.gerarrelatoriomovimentacao.GerarRelatorioMemorandoMovimentacaoPDFInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.gerarrelatoriomovimentacao.GerarRelatorioMemorandoMovimentacaoPDFOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.gerarrelatoriomovimentacao.GerarRelatorioMemorandoMovimentacaoPDFUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.gerarrelatoriomovimentacao.converter.GerarRelatorioMemorandoMovimentacaoPDFOutputDataConverter;
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
public class GerarRelatorioMemorandoMovimentacaoPDFUseCaseTest {

    @InjectMocks
    GerarRelatorioMemorandoMovimentacaoPDFUseCaseImpl useCase;

    @Mock
    GerarNumeroMemorandoUseCase gerarNumeroMemorandoUseCase;

    @Mock
    MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    LancamentosContabeisDataProvider lancamentosContabeisDataProvider;

    @Mock
    AmortizacaoDataProvider amortizacaoDataProvider;

    @Mock
    SistemaDeRelatoriosIntegration sistemaDeRelatoriosIntegration;

    @InjectMocks
    GerarRelatorioMemorandoMovimentacaoPDFOutputDataConverter outputDataConverter;

    @Mock
    private BuscarParametrosUseCase buscarParametrosUseCase;

    @Before
    public void gerarInstanciaDoUseCase(){
        useCase = new GerarRelatorioMemorandoMovimentacaoPDFUseCaseImpl(movimentacaoDataProvider, lancamentosContabeisDataProvider, amortizacaoDataProvider,gerarNumeroMemorandoUseCase,sistemaDeRelatoriosIntegration,outputDataConverter,buscarParametrosUseCase);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdNaoForPassadoPorParametro(){
        useCase.executar(new GerarRelatorioMemorandoMovimentacaoPDFInputData());
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveFalharCasoMovimentacaoNaoSejaEncontrada(){
        Mockito.when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        useCase.executar(new GerarRelatorioMemorandoMovimentacaoPDFInputData(1L));
    }

    @Test
    public void deveRetornarRelatorioMemorandoMovimentacaoPDFAguardandoRecebimento(){
        Mockito.when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Movimentacao
                .builder()
                .id(1L)
                .codigo("00005")
                .situacao(Movimentacao.Situacao.AGUARDANDO_RECEBIMENTO)
                .orgaoDestino(UnidadeOrganizacional.builder().sigla("AGEPAN").build())
                .orgaoOrigem(UnidadeOrganizacional.builder().id(5L).sigla("AGETRAN").nome("Agencia Municipal de Transporter e Transito").build())
                .motivo("Movimentação está em desuso")
                .dataCadastro(LocalDateTime.of(2020, Month.AUGUST,20,8,24))
                .dataDeEnvio(LocalDateTime.of(2020, Month.AUGUST,25,10,24))
                .patrimonio(Patrimonio
                    .builder()
                    .id(2L)
                    .numero("00005")
                    .tipo(Patrimonio.Tipo.SOFTWARES)
                    .orgao(UnidadeOrganizacional.builder().id(5L).sigla("AGETRAN").build())
                    .valorLiquido(BigDecimal.valueOf(2.500))
                    .situacao(Patrimonio.Situacao.ATIVO)
                    .valorAquisicao(BigDecimal.valueOf(1.500))
                    .anoMemorando(2020)
                    .numeroMemorando("000001")
                    .nome("Intelijj")
                    .dataAtivacao(LocalDateTime.of(2020,Month.AUGUST,03,10,45))
                    .contaContabil(ContaContabil
                        .builder()
                        .codigo("0005")
                        .id(2L)
                        .situacao(ContaContabil.Situacao.ATIVO)
                        .descricao("OUTROS DIREITOS-BENS INTANGIVEIS")
                        .build())
                    .build())
                .tipo(Movimentacao.Tipo.DOACAO_ENTRE_ORGAOS)
                .build()));

        Mockito.when(gerarNumeroMemorandoUseCase.executar()).thenReturn(GerarNumeroMemorandoOutputData.builder().numeroMemorando("000001").build());

        Mockito.when(buscarParametrosUseCase.executar()).thenReturn(BuscarParametrosOutputData.builder().mensagemRodapeMemorandoMovimentacao("Esse memorando ainda não tem valor contábil final, visto que a movimentação encontra-se aguardando recebimento para finalização").build());

        Mockito.when(movimentacaoDataProvider.salvar(any(Movimentacao.class)))
            .thenReturn(Movimentacao.builder().build());

        Mockito.when(sistemaDeRelatoriosIntegration.gerarRelatorioMemorandoMovimentacaoPDF(any(RelatorioMemorandoMovimentacao.class)))
            .thenReturn(
                Arquivo.builder()
                    .nome("relatoriomemorandomovimentacao.pdf")
                    .contentType("application/pdf")
                    .build()
            );

        GerarRelatorioMemorandoMovimentacaoPDFOutputData outputData = useCase.executar(new GerarRelatorioMemorandoMovimentacaoPDFInputData(1L));

        assertEquals("relatoriomemorandomovimentacao.pdf", outputData.getNome());
        assertEquals("application/pdf", outputData.getContentType());
    }


    @Test
    public void deveRetornarRelatorioMemorandoMovimentacaoPDFFinalizadaComAmortizacaoSemMovimentacaoNoMesmoMesAno() {
        Mockito.when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Movimentacao
                .builder()
                .id(1L)
                .codigo("00005")
                .situacao(Movimentacao.Situacao.FINALIZADO)
                .orgaoDestino(UnidadeOrganizacional.builder().sigla("AGEPAN").build())
                .orgaoOrigem(UnidadeOrganizacional.builder().id(5L).sigla("AGETRAN").nome("Agencia Municipal de Transporter e Transito").build())
                .motivo("Movimentação está em desuso")
                .dataCadastro(LocalDateTime.of(2020, Month.AUGUST, 20, 8, 24))
                .dataDeEnvio(LocalDateTime.of(2020, Month.AUGUST, 25, 10, 24))
                .dataDeFinalizacao(LocalDateTime.of(2020, Month.AUGUST, 27, 10, 24))
                .patrimonio(Patrimonio
                    .builder()
                    .id(2L)
                    .numero("00005")
                    .tipo(Patrimonio.Tipo.SOFTWARES)
                    .orgao(UnidadeOrganizacional.builder().id(5L).sigla("AGETRAN").build())
                    .valorLiquido(BigDecimal.valueOf(2.500))
                    .situacao(Patrimonio.Situacao.ATIVO)
                    .valorAquisicao(BigDecimal.valueOf(1.500))
                    .anoMemorando(2020)
                    .numeroMemorando("000001")
                    .nome("Intelijj")
                    .dataAtivacao(LocalDateTime.of(2020, Month.AUGUST, 03, 10, 45))
                    .contaContabil(ContaContabil
                        .builder()
                        .codigo("0005")
                        .id(2L)
                        .situacao(ContaContabil.Situacao.ATIVO)
                        .descricao("OUTROS DIREITOS-BENS INTANGIVEIS")
                        .build())
                    .build())
                .tipo(Movimentacao.Tipo.DOACAO_ENTRE_ORGAOS)
                .build()));

        Mockito.when(amortizacaoDataProvider.existePorPatrimonio(2L)).thenReturn(Boolean.TRUE);

        Mockito.when(amortizacaoDataProvider.existePorPatrimonioEOrgaoNoPeriodo(any(Long.class), any(Long.class), any(LocalDateTime.class))).thenReturn(Boolean.TRUE);

        Mockito.when(amortizacaoDataProvider.buscarPrimeiraPorOrgaoEPatrimonio(any(Long.class), any(Long.class))).thenReturn(Optional.of(
            Amortizacao
                .builder()
                .id(1L)
                .valorAnterior(BigDecimal.valueOf(10000))
                .build()
        ));

        Mockito.when(gerarNumeroMemorandoUseCase.executar()).thenReturn(GerarNumeroMemorandoOutputData.builder().numeroMemorando("000001").build());

        Mockito.when(lancamentosContabeisDataProvider.buscarPorMovimentacaoETipoLancamento(1L, "DEBITO")).thenReturn(Optional.of(
            LancamentosContabeis
                .builder()
                .id(2L)
                .movimentacao(Movimentacao
                    .builder()
                    .id(2L)
                    .build())
                .orgao(UnidadeOrganizacional
                    .builder()
                    .id(1L)
                    .build())
                .patrimonio(Patrimonio
                    .builder()
                    .id(1L)
                    .build())
                .valorLiquido(BigDecimal.valueOf(10000))
                .dataLancamento(LocalDateTime.now())
                .tipoLancamento(LancamentosContabeis.TipoLancamento.DEBITO)
                .build()));

        Mockito.when(lancamentosContabeisDataProvider.buscarLancamentoContabilAnteriorCredito(any(Long.class), any(Long.class), any(LocalDateTime.class))).thenReturn(Optional.of(
            LancamentosContabeis
                .builder()
                .id(1L)
                .movimentacao(Movimentacao
                    .builder()
                    .id(1L)
                    .build())
                .orgao(UnidadeOrganizacional
                    .builder()
                    .id(1L)
                    .build())
                .patrimonio(Patrimonio
                    .builder()
                    .id(1L)
                    .build())
                .valorLiquido(BigDecimal.valueOf(100000))
                .dataLancamento(LocalDateTime.now().withMonth(Month.MARCH.getValue()).withDayOfMonth(11).withYear(2020))
                .tipoLancamento(LancamentosContabeis.TipoLancamento.CREDITO)
                .build()));

        Mockito.when(movimentacaoDataProvider.salvar(any(Movimentacao.class)))
            .thenReturn(Movimentacao.builder().build());

        Mockito.when(sistemaDeRelatoriosIntegration.gerarRelatorioMemorandoMovimentacaoPDF(any(RelatorioMemorandoMovimentacao.class)))
            .thenReturn(
                Arquivo.builder()
                    .nome("relatoriomemorandomovimentacao.pdf")
                    .contentType("application/pdf")
                    .build()
            );

        GerarRelatorioMemorandoMovimentacaoPDFOutputData outputData = useCase.executar(new GerarRelatorioMemorandoMovimentacaoPDFInputData(1L));

        assertEquals("relatoriomemorandomovimentacao.pdf", outputData.getNome());
        assertEquals("application/pdf", outputData.getContentType());
    }

    @Test
    public void deveRetornarRelatorioMemorandoMovimentacaoPDFFinalizadaSemAmortizacaoSemMovimentacaoNoMesmoMesAno() {
        Mockito.when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Movimentacao
                .builder()
                .id(1L)
                .codigo("00005")
                .situacao(Movimentacao.Situacao.FINALIZADO)
                .orgaoDestino(UnidadeOrganizacional.builder().sigla("AGEPAN").build())
                .orgaoOrigem(UnidadeOrganizacional.builder().id(5L).sigla("AGETRAN").nome("Agencia Municipal de Transporter e Transito").build())
                .motivo("Movimentação está em desuso")
                .dataCadastro(LocalDateTime.of(2020, Month.AUGUST, 20, 8, 24))
                .dataDeEnvio(LocalDateTime.of(2020, Month.AUGUST, 25, 10, 24))
                .dataDeFinalizacao(LocalDateTime.of(2020, Month.AUGUST, 27, 10, 24))
                .patrimonio(Patrimonio
                    .builder()
                    .id(2L)
                    .numero("00005")
                    .tipo(Patrimonio.Tipo.SOFTWARES)
                    .orgao(UnidadeOrganizacional.builder().id(5L).sigla("AGETRAN").build())
                    .valorLiquido(BigDecimal.valueOf(2.500))
                    .situacao(Patrimonio.Situacao.ATIVO)
                    .valorAquisicao(BigDecimal.valueOf(1.500))
                    .anoMemorando(2020)
                    .numeroMemorando("000001")
                    .nome("Intelijj")
                    .dataAtivacao(LocalDateTime.of(2020, Month.AUGUST, 03, 10, 45))
                    .contaContabil(ContaContabil
                        .builder()
                        .codigo("0005")
                        .id(2L)
                        .situacao(ContaContabil.Situacao.ATIVO)
                        .descricao("OUTROS DIREITOS-BENS INTANGIVEIS")
                        .build())
                    .build())
                .tipo(Movimentacao.Tipo.DOACAO_ENTRE_ORGAOS)
                .build()));

        Mockito.when(amortizacaoDataProvider.existePorPatrimonio(2L)).thenReturn(Boolean.TRUE);

        Mockito.when(gerarNumeroMemorandoUseCase.executar()).thenReturn(GerarNumeroMemorandoOutputData.builder().numeroMemorando("000001").build());

        Mockito.when(lancamentosContabeisDataProvider.buscarPorMovimentacaoETipoLancamento(1L, "DEBITO")).thenReturn(Optional.of(
            LancamentosContabeis
                .builder()
                .id(2L)
                .movimentacao(Movimentacao
                    .builder()
                    .id(2L)
                    .build())
                .orgao(UnidadeOrganizacional
                    .builder()
                    .id(1L)
                    .build())
                .patrimonio(Patrimonio
                    .builder()
                    .id(1L)
                    .build())
                .valorLiquido(BigDecimal.valueOf(10000))
                .dataLancamento(LocalDateTime.now())
                .tipoLancamento(LancamentosContabeis.TipoLancamento.DEBITO)
                .build()));

        Mockito.when(lancamentosContabeisDataProvider.buscarLancamentoContabilAnteriorCredito(any(Long.class), any(Long.class), any(LocalDateTime.class))).thenReturn(Optional.of(
            LancamentosContabeis
                .builder()
                .id(1L)
                .movimentacao(Movimentacao
                    .builder()
                    .id(1L)
                    .build())
                .orgao(UnidadeOrganizacional
                    .builder()
                    .id(1L)
                    .build())
                .patrimonio(Patrimonio
                    .builder()
                    .id(1L)
                    .build())
                .valorLiquido(BigDecimal.valueOf(100000))
                .dataLancamento(LocalDateTime.now().withMonth(Month.MARCH.getValue()).withDayOfMonth(11).withYear(2020))
                .tipoLancamento(LancamentosContabeis.TipoLancamento.CREDITO)
                .build()));

        Mockito.when(movimentacaoDataProvider.salvar(any(Movimentacao.class)))
            .thenReturn(Movimentacao.builder().build());

        Mockito.when(sistemaDeRelatoriosIntegration.gerarRelatorioMemorandoMovimentacaoPDF(any(RelatorioMemorandoMovimentacao.class)))
            .thenReturn(
                Arquivo.builder()
                    .nome("relatoriomemorandomovimentacao.pdf")
                    .contentType("application/pdf")
                    .build()
            );

        GerarRelatorioMemorandoMovimentacaoPDFOutputData outputData = useCase.executar(new GerarRelatorioMemorandoMovimentacaoPDFInputData(1L));

        assertEquals("relatoriomemorandomovimentacao.pdf", outputData.getNome());
        assertEquals("application/pdf", outputData.getContentType());
    }

    @Test
    public void deveRetornarRelatorioMemorandoMovimentacaoPDFFinalizadaSemAmortizacaoComMovimentacaoNoMesmoMesAno() {
        Mockito.when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Movimentacao
                .builder()
                .id(1L)
                .codigo("00005")
                .situacao(Movimentacao.Situacao.FINALIZADO)
                .orgaoDestino(UnidadeOrganizacional.builder().sigla("AGEPAN").build())
                .orgaoOrigem(UnidadeOrganizacional.builder().id(5L).sigla("AGETRAN").nome("Agencia Municipal de Transporter e Transito").build())
                .motivo("Movimentação está em desuso")
                .dataCadastro(LocalDateTime.of(2020, Month.AUGUST, 20, 8, 24))
                .dataDeEnvio(LocalDateTime.of(2020, Month.AUGUST, 25, 10, 24))
                .dataDeFinalizacao(LocalDateTime.of(2020, Month.AUGUST, 27, 10, 24))
                .patrimonio(Patrimonio
                    .builder()
                    .id(2L)
                    .numero("00005")
                    .tipo(Patrimonio.Tipo.SOFTWARES)
                    .orgao(UnidadeOrganizacional.builder().id(5L).sigla("AGETRAN").build())
                    .valorLiquido(BigDecimal.valueOf(2.500))
                    .situacao(Patrimonio.Situacao.ATIVO)
                    .valorAquisicao(BigDecimal.valueOf(1.500))
                    .anoMemorando(2020)
                    .numeroMemorando("000001")
                    .nome("Intelijj")
                    .dataAtivacao(LocalDateTime.of(2020, Month.AUGUST, 03, 10, 45))
                    .contaContabil(ContaContabil
                        .builder()
                        .codigo("0005")
                        .id(2L)
                        .situacao(ContaContabil.Situacao.ATIVO)
                        .descricao("OUTROS DIREITOS-BENS INTANGIVEIS")
                        .build())
                    .build())
                .tipo(Movimentacao.Tipo.DOACAO_ENTRE_ORGAOS)
                .build()));

        Mockito.when(amortizacaoDataProvider.existePorPatrimonio(2L)).thenReturn(Boolean.TRUE);

        Mockito.when(amortizacaoDataProvider.existePorPatrimonioEOrgaoNoPeriodo(any(Long.class), any(Long.class), any(LocalDateTime.class))).thenReturn(Boolean.FALSE);

        Mockito.when(gerarNumeroMemorandoUseCase.executar()).thenReturn(GerarNumeroMemorandoOutputData.builder().numeroMemorando("000001").build());

        Mockito.when(lancamentosContabeisDataProvider.buscarPorMovimentacaoETipoLancamento(1L, "DEBITO")).thenReturn(Optional.of(
            LancamentosContabeis
                .builder()
                .id(2L)
                .movimentacao(Movimentacao
                    .builder()
                    .id(2L)
                    .build())
                .orgao(UnidadeOrganizacional
                    .builder()
                    .id(1L)
                    .build())
                .patrimonio(Patrimonio
                    .builder()
                    .id(1L)
                    .build())
                .valorLiquido(BigDecimal.valueOf(10000))
                .dataLancamento(LocalDateTime.now().withMonth(Month.MARCH.getValue()).withDayOfMonth(11).withYear(2020))
                .tipoLancamento(LancamentosContabeis.TipoLancamento.DEBITO)
                .build()));

        Mockito.when(lancamentosContabeisDataProvider.buscarLancamentoContabilAnteriorCredito(any(Long.class), any(Long.class), any(LocalDateTime.class))).thenReturn(Optional.of(
            LancamentosContabeis
                .builder()
                .id(1L)
                .movimentacao(Movimentacao
                    .builder()
                    .id(1L)
                    .build())
                .orgao(UnidadeOrganizacional
                    .builder()
                    .id(1L)
                    .build())
                .patrimonio(Patrimonio
                    .builder()
                    .id(1L)
                    .build())
                .valorLiquido(BigDecimal.valueOf(100000))
                .dataLancamento(LocalDateTime.now().withMonth(Month.MARCH.getValue()).withDayOfMonth(11).withYear(2020))
                .tipoLancamento(LancamentosContabeis.TipoLancamento.CREDITO)
                .build()));

        Mockito.when(movimentacaoDataProvider.salvar(any(Movimentacao.class)))
            .thenReturn(Movimentacao.builder().build());

        Mockito.when(sistemaDeRelatoriosIntegration.gerarRelatorioMemorandoMovimentacaoPDF(any(RelatorioMemorandoMovimentacao.class)))
            .thenReturn(
                Arquivo.builder()
                    .nome("relatoriomemorandomovimentacao.pdf")
                    .contentType("application/pdf")
                    .build()
            );

        GerarRelatorioMemorandoMovimentacaoPDFOutputData outputData = useCase.executar(new GerarRelatorioMemorandoMovimentacaoPDFInputData(1L));

        assertEquals("relatoriomemorandomovimentacao.pdf", outputData.getNome());
        assertEquals("application/pdf", outputData.getContentType());
    }
}
