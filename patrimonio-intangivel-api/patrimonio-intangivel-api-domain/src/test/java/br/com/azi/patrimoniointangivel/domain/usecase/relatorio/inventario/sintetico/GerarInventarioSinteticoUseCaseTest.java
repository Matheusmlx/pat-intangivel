package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.sintetico;

import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;
import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.Job;
import br.com.azi.patrimoniointangivel.domain.entity.LancamentosContabeisAgrupado;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.RelatorioSintetico;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.LancamentosContabeisDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.RelatorioSinteticoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.UnidadeOrganizacionalDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.sintetico.converter.GerarInventarioSinteticoOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.sintetico.exception.AmortizacaoEmAndamentoException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class GerarInventarioSinteticoUseCaseTest {

    private RelatorioSinteticoDataProvider relatorioDataProvider;
    private LancamentosContabeisDataProvider lancamentosContabeisDataProvider;
    private SistemaDeRelatoriosIntegration sistemaDeRelatoriosIntegration;
    private UnidadeOrganizacionalDataProvider unidadeOrganizacionalDataProvider;

    @Before
    public void start() {
        relatorioDataProvider = Mockito.mock(RelatorioSinteticoDataProvider.class);
        lancamentosContabeisDataProvider = Mockito.mock(LancamentosContabeisDataProvider.class);
        sistemaDeRelatoriosIntegration = Mockito.mock(SistemaDeRelatoriosIntegration.class);
        unidadeOrganizacionalDataProvider = Mockito.mock(UnidadeOrganizacionalDataProvider.class);
    }

    @Test
    public void deveRetornarRelatorioSinteticoPDF() {
        Mockito.when(relatorioDataProvider.buscaRelatorioSintetico(any(Long.class), any(LocalDateTime.class)))
            .thenReturn(
                List.of(
                    RelatorioSintetico.builder()
                        .orgao(UnidadeOrganizacional.builder().sigla("ABC").nome("NOME ORGAO").id(129L).build())
                        .orgaoAmortizacao(UnidadeOrganizacional.builder().sigla("ABC").nome("NOME ORGAO").id(129L).build())
                        .patrimonioId(1L)
                        .valorLiquido(BigDecimal.valueOf(10))
                        .valorAquisicao(BigDecimal.valueOf(10))
                        .dataAtivacao(LocalDateTime.now().withMonth(Month.JANUARY.getValue()).withDayOfMonth(8).withYear(2020))
                        .dataCadastro(LocalDateTime.now().withMonth(Month.JANUARY.getValue()).withDayOfMonth(8).withYear(2020))
                        .contaContabil(ContaContabil.builder()
                            .codigo("4800100")
                            .descricao("conta contabil")
                            .build())
                        .valorAmortizadoMensal(BigDecimal.valueOf(10))
                        .valorAmortizadoAcumulado(BigDecimal.valueOf(10))
                        .build(),
                    RelatorioSintetico.builder()
                        .orgao(UnidadeOrganizacional.builder().sigla("ABC").nome("NOME ORGAO").id(129L).build())
                        .orgaoAmortizacao(UnidadeOrganizacional.builder().sigla("ABC").nome("NOME ORGAO").id(129L).build())
                        .patrimonioId(1L)
                        .valorLiquido(BigDecimal.valueOf(10))
                        .valorAquisicao(BigDecimal.valueOf(10))
                        .dataAtivacao(LocalDateTime.now())
                        .dataCadastro(LocalDateTime.now())
                        .contaContabil(ContaContabil.builder()
                            .codigo("4800100")
                            .descricao("conta contabil")
                            .build())
                        .valorAmortizadoMensal(BigDecimal.valueOf(10))
                        .valorAmortizadoAcumulado(BigDecimal.valueOf(10))
                        .build())
            );

        Mockito.when(lancamentosContabeisDataProvider.buscarLancamentosContabeisAgrupados(any(LocalDateTime.class)))
            .thenReturn(
                List.of(
                    LancamentosContabeisAgrupado.builder()
                        .patrimonio(Patrimonio.builder().id(1L).build())
                        .orgao(UnidadeOrganizacional.builder().sigla("ABC").nome("NOME ORGAO").id(129L).build())
                        .maiorData(LocalDateTime.now().withMonth(Month.NOVEMBER.getValue()).withDayOfMonth(8).withYear(2020))
                        .maiorValorLiquido(BigDecimal.valueOf(5))
                        .build(),
                    LancamentosContabeisAgrupado.builder()
                        .patrimonio(Patrimonio.builder().id(2L).build())
                        .orgao(UnidadeOrganizacional.builder().sigla("ABC").nome("NOME ORGAO").id(129L).build())
                        .maiorData(LocalDateTime.now())
                        .maiorValorLiquido(BigDecimal.valueOf(6))
                        .build()
                )
            );

        Mockito.when(sistemaDeRelatoriosIntegration.gerarRelatorioInventarioSinteticoPDF(any(List.class), any(List.class), any(UnidadeOrganizacional.class), any(LocalDateTime.class)))
            .thenReturn(
                Arquivo.builder()
                    .nome("inventariosintetico.pdf")
                    .contentType("application/pdf")
                    .build()
            );

        Mockito.when(unidadeOrganizacionalDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(
                Optional.of(UnidadeOrganizacional.builder()
                    .id(1L)
                    .nome("orgão")
                    .sigla("Sigla")
                    .build()
                )
            );

        GerarInventarioSinteticoUseCaseImpl useCase = new GerarInventarioSinteticoUseCaseImpl(
            relatorioDataProvider,
            lancamentosContabeisDataProvider,
            sistemaDeRelatoriosIntegration,
            unidadeOrganizacionalDataProvider,
            new GerarInventarioSinteticoOutputDataConverter()
        );

        Job job = Job.getInstance();
        job.setStatus(Job.Status.PARADO);

        GerarInventarioSinteticoOutputData outputData = useCase.executar(new GerarInventarioSinteticoInputData("PDF", (long) 129, "2020-04"));

        Assert.assertEquals("inventariosintetico.pdf", outputData.getNome());
        Assert.assertEquals("application/pdf", outputData.getContentType());
    }

    @Test
    public void deveRetornarRelatorioSinteticoXLS() {
        Mockito.when(relatorioDataProvider.buscaRelatorioSintetico(any(Long.class), any(LocalDateTime.class)))
            .thenReturn(
                List.of(
                    RelatorioSintetico.builder()
                        .orgao(UnidadeOrganizacional.builder().sigla("ABC").nome("NOME ORGAO").id(129L).build())
                        .orgaoAmortizacao(UnidadeOrganizacional.builder().sigla("ABC").nome("NOME ORGAO").id(129L).build())
                        .patrimonioId(1L)
                        .valorLiquido(BigDecimal.valueOf(10))
                        .valorAquisicao(BigDecimal.valueOf(10))
                        .dataAtivacao(LocalDateTime.now().withMonth(Month.JANUARY.getValue()).withDayOfMonth(8).withYear(2020))
                        .dataCadastro(LocalDateTime.now().withMonth(Month.JANUARY.getValue()).withDayOfMonth(8).withYear(2020))
                        .contaContabil(ContaContabil.builder()
                            .codigo("4800100")
                            .descricao("conta contabil")
                            .build())
                        .valorAmortizadoMensal(BigDecimal.valueOf(10))
                        .valorAmortizadoAcumulado(BigDecimal.valueOf(10))
                        .build(),
                    RelatorioSintetico.builder()
                        .orgao(UnidadeOrganizacional.builder().sigla("ABC").nome("NOME ORGAO").id(129L).build())
                        .orgaoAmortizacao(UnidadeOrganizacional.builder().sigla("ABC").nome("NOME ORGAO").id(129L).build())
                        .patrimonioId(1L)
                        .valorLiquido(BigDecimal.valueOf(10))
                        .valorAquisicao(BigDecimal.valueOf(10))
                        .dataAtivacao(LocalDateTime.now())
                        .dataCadastro(LocalDateTime.now())
                        .contaContabil(ContaContabil.builder()
                            .codigo("4800100")
                            .descricao("conta contabil")
                            .build())
                        .valorAmortizadoMensal(BigDecimal.valueOf(10))
                        .valorAmortizadoAcumulado(BigDecimal.valueOf(10))
                        .build())
            );

        Mockito.when(lancamentosContabeisDataProvider.buscarLancamentosContabeisAgrupados(any(LocalDateTime.class)))
            .thenReturn(
                List.of(
                    LancamentosContabeisAgrupado.builder()
                        .patrimonio(Patrimonio.builder().id(1L).build())
                        .orgao(UnidadeOrganizacional.builder().sigla("ABC").nome("NOME ORGAO").id(129L).build())
                        .maiorData(LocalDateTime.now().withMonth(Month.NOVEMBER.getValue()).withDayOfMonth(8).withYear(2020))
                        .maiorValorLiquido(BigDecimal.valueOf(5))
                        .build(),
                    LancamentosContabeisAgrupado.builder()
                        .patrimonio(Patrimonio.builder().id(2L).build())
                        .orgao(UnidadeOrganizacional.builder().sigla("ABC").nome("NOME ORGAO").id(129L).build())
                        .maiorData(LocalDateTime.now())
                        .maiorValorLiquido(BigDecimal.valueOf(6))
                        .build()
                )
            );

        Mockito.when(sistemaDeRelatoriosIntegration.gerarRelatorioInventarioSinteticoXLS(any(List.class), any(List.class), any(UnidadeOrganizacional.class), any(LocalDateTime.class)))
            .thenReturn(
                Arquivo.builder()
                    .nome("inventariosintetico.xls")
                    .contentType("application/xls")
                    .build()
            );

        Mockito.when(unidadeOrganizacionalDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(
                Optional.of(UnidadeOrganizacional.builder()
                    .id(1L)
                    .nome("orgão")
                    .sigla("Sigla")
                    .build()
                )
            );

        GerarInventarioSinteticoUseCaseImpl useCase = new GerarInventarioSinteticoUseCaseImpl(
            relatorioDataProvider,
            lancamentosContabeisDataProvider,
            sistemaDeRelatoriosIntegration,
            unidadeOrganizacionalDataProvider,
            new GerarInventarioSinteticoOutputDataConverter()
        );

        Job job = Job.getInstance();
        job.setStatus(Job.Status.PARADO);

        GerarInventarioSinteticoOutputData outputData = useCase.executar(new GerarInventarioSinteticoInputData("XLS", (long) 129, "2020-04"));

        Assert.assertEquals(outputData.getNome(), "inventariosintetico.xls");
        Assert.assertEquals(outputData.getContentType(), "application/xls");
    }

    @Test(expected = AmortizacaoEmAndamentoException.class)
    public void deveFalharQuandoJobEstiverEmAndamento() {

        GerarInventarioSinteticoUseCaseImpl useCase = new GerarInventarioSinteticoUseCaseImpl(
            relatorioDataProvider,
            lancamentosContabeisDataProvider,
            sistemaDeRelatoriosIntegration,
            unidadeOrganizacionalDataProvider,
            new GerarInventarioSinteticoOutputDataConverter()
        );

        Job job = Job.getInstance();
        job.setStatus(Job.Status.EM_ANDAMENTO);

        useCase.executar(new GerarInventarioSinteticoInputData("PDF", (long) 129, "2020-04"));

    }
}
