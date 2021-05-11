package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando;

import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;
import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.Fornecedor;
import br.com.azi.patrimoniointangivel.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.RelatorioMemorando;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.LancamentosContabeisDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.memorando.GerarNumeroMemorandoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.memorando.GerarNumeroMemorandoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.gerarrelatorio.GerarRelatorioMemorandoPDFInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.gerarrelatorio.GerarRelatorioMemorandoPDFOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.gerarrelatorio.GerarRelatorioMemorandoPDFUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.gerarrelatorio.converter.GerarRelatorioMemorandoPDFOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.historicomemorando.GerarHistoricoMemorandoUseCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GerarRelatorioMemorandoPDFUseCaseTest {

    @InjectMocks
    private GerarRelatorioMemorandoPDFUseCaseImpl useCase;

    @InjectMocks
    private GerarRelatorioMemorandoPDFOutputDataConverter outputDataConverter;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    private LancamentosContabeisDataProvider lancamentosContabeisDataProvider;

    @Mock
    private SistemaDeRelatoriosIntegration sistemaDeRelatoriosIntegration;

    @Mock
    private GerarNumeroMemorandoUseCase gerarNumeroMemorandoUseCase;

    @Mock
    private GerarHistoricoMemorandoUseCase gerarHistoricoMemorandoUseCase;

    @Before
    public void start() {
        useCase = new GerarRelatorioMemorandoPDFUseCaseImpl(patrimonioDataProvider, sistemaDeRelatoriosIntegration, outputDataConverter, gerarNumeroMemorandoUseCase, gerarHistoricoMemorandoUseCase, lancamentosContabeisDataProvider);
    }

    @Test
    public void deveRetornarRelatorioMemorandoPDF() {
        when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(
                Optional.of(Patrimonio.builder()
                    .id(1L)
                    .descricao("Descrição Teste memorando")
                    .numero("000000001")
                    .nome("Teste")
                    .notaLancamentoContabil(NotaLancamentoContabil.builder().numero("253383NL3973").dataLancamento(LocalDateTime.now().withYear(2031).withMonth(Month.JANUARY.getValue()).withDayOfMonth(3)).build())
                    .dataAtivacao(LocalDateTime.now().withYear(2031).withMonth(Month.JANUARY.getValue()).withDayOfMonth(2))
                    .valorAquisicao(BigDecimal.valueOf(1500))
                    .contaContabil(ContaContabil.builder().descricao("descrição conta contabil").build())
                    .fornecedor(Fornecedor.builder().nome("nome fornecedor").build())
                    .reconhecimento(Patrimonio.Reconhecimento.TRANSACAO_SEM_CONTRAPRESTACAO)
                    .orgao(UnidadeOrganizacional.builder().nome("nome orgao").sigla("sigla orgao").build())
                    .setor(UnidadeOrganizacional.builder().nome("nome setor").sigla("sigla setor").build())
                    .build()));

        when(gerarNumeroMemorandoUseCase.executar()).thenReturn(GerarNumeroMemorandoOutputData.builder().numeroMemorando("000001").build());

        when(sistemaDeRelatoriosIntegration.gerarRelatorioMemorandoPDF(any(RelatorioMemorando.class)))
            .thenReturn(
                Arquivo.builder()
                    .nome("relatoriomemorando.pdf")
                    .contentType("application/pdf")
                    .build()
            );

        GerarRelatorioMemorandoPDFOutputData outputData = useCase.executar(
            GerarRelatorioMemorandoPDFInputData
                .builder()
                .patrimonioId(1L)
                .build());

        assertEquals("relatoriomemorando.pdf", outputData.getNome());
        assertEquals("application/pdf", outputData.getContentType());
        assertEquals(null, outputData.getContent());
        assertEquals(null, outputData.getUri());
        assertEquals(null, outputData.getUrl());
    }

    @Test
    public void deveRetornarRelatorioMemorandoPDFSemData() {
        when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(
                Optional.of(Patrimonio.builder()
                    .id(1L)
                    .descricao("Descrição Teste memorando")
                    .numero("000000001")
                    .dataAtivacao(null)
                    .valorAquisicao(BigDecimal.valueOf(1500))
                    .contaContabil(ContaContabil.builder().descricao("descrição conta contabil").build())
                    .fornecedor(Fornecedor.builder().nome("nome fornecedor").build())
                    .reconhecimento(Patrimonio.Reconhecimento.TRANSACAO_SEM_CONTRAPRESTACAO)
                    .orgao(UnidadeOrganizacional.builder().nome("nome orgao").sigla("sigla orgao").build())
                    .notaLancamentoContabil(NotaLancamentoContabil.builder().numero("5555NL555555").build())
                    .setor(UnidadeOrganizacional.builder().nome("nome setor").sigla("sigla setor").build())
                    .build()));

        when(gerarNumeroMemorandoUseCase.executar()).thenReturn(GerarNumeroMemorandoOutputData.builder().numeroMemorando("000001").build());

        when(sistemaDeRelatoriosIntegration.gerarRelatorioMemorandoPDF(any(RelatorioMemorando.class)))
            .thenReturn(
                Arquivo.builder()
                    .nome("relatoriomemorando.pdf")
                    .contentType("application/pdf")
                    .build()
            );

        GerarRelatorioMemorandoPDFOutputData outputData = useCase.executar(new GerarRelatorioMemorandoPDFInputData(1L));

        assertEquals("relatoriomemorando.pdf", outputData.getNome());
        assertEquals("application/pdf", outputData.getContentType());
    }
}
