package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.analitico;

import br.com.azi.patrimoniointangivel.domain.entity.Amortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;
import br.com.azi.patrimoniointangivel.domain.entity.ConfigContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.Job;
import br.com.azi.patrimoniointangivel.domain.entity.LancamentosContabeis;
import br.com.azi.patrimoniointangivel.domain.entity.LancamentosContabeisAgrupado;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.RelatorioAnalitico;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.AmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.LancamentosContabeisDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.analitico.converter.GerarInventarioAnaliticoOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.analitico.exception.AmortizacaoEmAndamentoException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class GerarInventarioAnaliticoUseCaseTest {
    @Mock
    private SistemaDeRelatoriosIntegration sistemaDeRelatoriosIntegration;

    @Mock
    private AmortizacaoDataProvider amortizacaoDataProvider;

    @Mock
    private ConfigContaContabilDataProvider configContaContabilDataProvider;

    @Mock
    private LancamentosContabeisDataProvider lancamentosContabeisDataProvider;


    @Test(expected = AmortizacaoEmAndamentoException.class)
    public void deveFalharQuandoAmortizacaoEstiverEmAndamento() {
        GerarInventarioAnaliticoUseCaseImpl useCase = new GerarInventarioAnaliticoUseCaseImpl(
            sistemaDeRelatoriosIntegration,
            amortizacaoDataProvider,
            configContaContabilDataProvider,
            lancamentosContabeisDataProvider,
            new GerarInventarioAnaliticoOutputDataConverter()
        );

        Job job = Job.getInstance();
        job.setStatus(Job.Status.EM_ANDAMENTO);

        useCase.executar(new GerarInventarioAnaliticoInputData( "PDF",129L, "2020-04"));
    }


    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoPassarDados() {
        GerarInventarioAnaliticoUseCaseImpl useCase = new GerarInventarioAnaliticoUseCaseImpl(
            sistemaDeRelatoriosIntegration,
            amortizacaoDataProvider,
            configContaContabilDataProvider,
            lancamentosContabeisDataProvider,
            new GerarInventarioAnaliticoOutputDataConverter()
        );

        Job job = Job.getInstance();
        job.setStatus(Job.Status.PARADO);

        useCase.executar(new GerarInventarioAnaliticoInputData( ));
    }



    @Test
    public void deveRetornarRelatorioAnaliticoPDF() {
        GerarInventarioAnaliticoUseCaseImpl useCase = new GerarInventarioAnaliticoUseCaseImpl(
            sistemaDeRelatoriosIntegration,
            amortizacaoDataProvider,
            configContaContabilDataProvider,
            lancamentosContabeisDataProvider,
            new GerarInventarioAnaliticoOutputDataConverter()
        );

        Mockito.when(lancamentosContabeisDataProvider.buscarLancamentosContabeisAgrupadosPorOrgao(any(LocalDateTime.class), any(Long.class)))
            .thenReturn(
                Arrays.asList(
                    LancamentosContabeisAgrupado.builder()
                        .patrimonio(Patrimonio.builder()
                            .id(1L)
                            .tipo(Patrimonio.Tipo.SOFTWARES)
                            .nome("Patrimonio 1")
                            .numero("000000001")
                            .amortizavel(Boolean.TRUE)
                            .contaContabil(ContaContabil
                                .builder()
                                .id(1L)
                                .descricao("BENS INTANGIVEIS>SOFTWARE")
                                .codigo("124110100")
                                .build())
                            .build())
                        .orgao(UnidadeOrganizacional.builder().sigla("ABC").nome("NOME ORGAO").id(129L).build())
                        .maiorData(LocalDateTime.now().withMonth(Month.NOVEMBER.getValue()).withDayOfMonth(8).withYear(2020))
                        .maiorValorLiquido(BigDecimal.valueOf(5))
                        .build(),
                    LancamentosContabeisAgrupado.builder()
                        .patrimonio(Patrimonio.builder()
                            .id(2L)
                            .tipo(Patrimonio.Tipo.SOFTWARES)
                            .nome("Patrimonio 2")
                            .numero("000000002")
                            .amortizavel(Boolean.TRUE)
                            .contaContabil(ContaContabil
                                .builder()
                                .id(1L)
                                .descricao("BENS INTANGIVEIS>SOFTWARE")
                                .codigo("124110100")
                                .build())
                            .build())
                        .orgao(UnidadeOrganizacional.builder().sigla("ABC").nome("NOME ORGAO").id(129L).build())
                        .maiorData(LocalDateTime.now())
                        .maiorValorLiquido(BigDecimal.valueOf(6))
                        .build()
                )
            );

        Mockito.when(configContaContabilDataProvider.buscarAtualPorContaContabil(any(Long.class)))
            .thenReturn(Optional.of(ConfigContaContabil.builder()
                .id(1L)
                .tipo(ConfigContaContabil.Tipo.DEPRECIAVEL)
                .build()));

        Mockito.when(amortizacaoDataProvider.existePorPatrimonioAteDataLimite(any(Long.class), any(LocalDateTime.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(amortizacaoDataProvider.buscarPorPatrimonioEDataLimite(any(Long.class), any(LocalDateTime.class)))
            .thenReturn(Optional.of(Amortizacao.builder()
                .taxaAplicada(BigDecimal.ONE)
                .valorSubtraido(BigDecimal.TEN)
                .valorPosterior(BigDecimal.valueOf(90))
                .valorAnterior(BigDecimal.valueOf(100))
                .dataFinal(LocalDateTime.of(2020, 3, 31, 23, 59, 59))
                .dataInicial(LocalDateTime.of(2020, 3, 1, 0, 0, 0))
                .build()));

        Mockito.when( lancamentosContabeisDataProvider.buscarUltimoPorPatrimonioNoOrgaoAteDataReferencia(any(Long.class), any(Long.class), any(LocalDateTime.class)))
            .thenReturn(Optional.of(LancamentosContabeis.builder()
                .id(1L)
                .valorLiquido(BigDecimal.valueOf(150))
                .tipoLancamento(LancamentosContabeis.TipoLancamento.CREDITO)
                .build()));

        Mockito.when(sistemaDeRelatoriosIntegration.gerarRelatorioInventarioAnaliticoPDF(any(RelatorioAnalitico.class)))
            .thenReturn(
                Arquivo.builder()
                    .nome("inventarioanalitico.xls")
                    .contentType("application/xls")
                    .build());

        Job job = Job.getInstance();
        job.setStatus(Job.Status.PARADO);

        useCase.executar(new GerarInventarioAnaliticoInputData( "PDF",129L, "2020-04"));

        Mockito.verify(sistemaDeRelatoriosIntegration, Mockito.times(1)).gerarRelatorioInventarioAnaliticoPDF(any(RelatorioAnalitico.class));

    }


}
