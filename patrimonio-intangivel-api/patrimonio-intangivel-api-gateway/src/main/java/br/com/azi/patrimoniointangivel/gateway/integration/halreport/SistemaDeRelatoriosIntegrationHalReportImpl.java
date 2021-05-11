package br.com.azi.patrimoniointangivel.gateway.integration.halreport;


import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;
import br.com.azi.patrimoniointangivel.domain.entity.LancamentosContabeisAgrupado;
import br.com.azi.patrimoniointangivel.domain.entity.ListaPaginada;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.RelatorioAnalitico;
import br.com.azi.patrimoniointangivel.domain.entity.RelatorioMemorando;
import br.com.azi.patrimoniointangivel.domain.entity.RelatorioMemorandoMovimentacao;
import br.com.azi.patrimoniointangivel.domain.entity.RelatorioSintetico;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.entity.HalReportIntegrationProperties;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarinventarioanaliticopdf.GerarInventarioAnaliticoPDFUseCase;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarinventarioanaliticoxls.GerarInventarioAnaliticoXLSUseCase;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarinventariosinteticopdf.GerarInventarioSinteticoPDFUseCase;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarinventariosinteticoxls.GerarInventarioSinteticoXLSUseCase;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarrelatoriolistagempatrimonioxls.GerarRelatorioListagemPatrimonioXLSUseCase;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarrelatoriomemorandopdf.GerarMemorandoPDFUseCase;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarrelatoriomovimentacaomemorandopdf.GerarMemorandoMovimentacaoPDFUseCase;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class SistemaDeRelatoriosIntegrationHalReportImpl implements SistemaDeRelatoriosIntegration {

    private HalReportIntegrationProperties integrationProperties;

    private GerarInventarioAnaliticoPDFUseCase gerarInventarioAnaliticoPDFUseCase;

    private GerarInventarioAnaliticoXLSUseCase gerarInventarioAnaliticoXLSUseCase;

    private GerarInventarioSinteticoPDFUseCase gerarInventarioSinteticoPDFUseCase;


    private GerarInventarioSinteticoXLSUseCase gerarInventarioSinteticoXLSUseCase;

    private GerarRelatorioListagemPatrimonioXLSUseCase gerarRelatorioListagemPatrimonioXLSUseCase;

    private GerarMemorandoPDFUseCase gerarMemorandoPDFUseCase;

    private GerarMemorandoMovimentacaoPDFUseCase gerarMemorandoMovimentacaoPDFUseCase;

    @Override
    public Arquivo gerarRelatorioInventarioAnaliticoPDF(RelatorioAnalitico registros) {
        return gerarInventarioAnaliticoPDFUseCase.executar(integrationProperties, registros);
    }

    @Override
    public Arquivo gerarRelatorioInventarioAnaliticoXLS(RelatorioAnalitico relatorio) {
        return gerarInventarioAnaliticoXLSUseCase.executar(integrationProperties, relatorio);
    }

    @Override
    public Arquivo gerarRelatorioInventarioSinteticoPDF(List<RelatorioSintetico> registros, List<LancamentosContabeisAgrupado> lancamentosContabeisAgrupado, UnidadeOrganizacional orgaoRelatorio, LocalDateTime dataFinal) {
        return gerarInventarioSinteticoPDFUseCase.executar(integrationProperties, registros, lancamentosContabeisAgrupado, orgaoRelatorio, dataFinal);
    }

    @Override
    public Arquivo gerarRelatorioInventarioSinteticoXLS(List<RelatorioSintetico> registros, List<LancamentosContabeisAgrupado> lancamentosContabeisAgrupado, UnidadeOrganizacional orgaoRelatorio, LocalDateTime dataFinal) {
        return gerarInventarioSinteticoXLSUseCase.executar(integrationProperties, registros, lancamentosContabeisAgrupado, orgaoRelatorio, dataFinal);
    }

    @Override
    public Arquivo gerarRelatorioListagemPatrimoniosXLS(ListaPaginada<Patrimonio> registros) {
        return gerarRelatorioListagemPatrimonioXLSUseCase.executar(integrationProperties, registros);
    }

    @Override
    public Arquivo gerarRelatorioMemorandoPDF(RelatorioMemorando relatorioMemorando) {
        return gerarMemorandoPDFUseCase.executar(integrationProperties, relatorioMemorando);
    }

    @Override
    public Arquivo gerarRelatorioMemorandoMovimentacaoPDF(RelatorioMemorandoMovimentacao relatorio) {
        return gerarMemorandoMovimentacaoPDFUseCase.executar(integrationProperties, relatorio);
    }
}
