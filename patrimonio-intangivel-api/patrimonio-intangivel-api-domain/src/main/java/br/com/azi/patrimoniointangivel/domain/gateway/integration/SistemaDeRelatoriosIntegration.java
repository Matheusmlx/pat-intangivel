package br.com.azi.patrimoniointangivel.domain.gateway.integration;


import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;
import br.com.azi.patrimoniointangivel.domain.entity.LancamentosContabeisAgrupado;
import br.com.azi.patrimoniointangivel.domain.entity.ListaPaginada;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.RelatorioAnalitico;
import br.com.azi.patrimoniointangivel.domain.entity.RelatorioMemorando;
import br.com.azi.patrimoniointangivel.domain.entity.RelatorioMemorandoMovimentacao;
import br.com.azi.patrimoniointangivel.domain.entity.RelatorioSintetico;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;

import java.time.LocalDateTime;
import java.util.List;

public interface SistemaDeRelatoriosIntegration {

    Arquivo gerarRelatorioInventarioAnaliticoPDF(RelatorioAnalitico relatorio);

    Arquivo gerarRelatorioInventarioAnaliticoXLS(RelatorioAnalitico relatorio);

    Arquivo gerarRelatorioInventarioSinteticoPDF(List<RelatorioSintetico> registros, List<LancamentosContabeisAgrupado> lancamentosContabeisAgrupado, UnidadeOrganizacional orgaoRelatorio,  LocalDateTime dataFinal);

    Arquivo gerarRelatorioInventarioSinteticoXLS(List<RelatorioSintetico> registros, List<LancamentosContabeisAgrupado> lancamentosContabeisAgrupado, UnidadeOrganizacional orgaoRelatorio, LocalDateTime dataFinal);

    Arquivo gerarRelatorioListagemPatrimoniosXLS(ListaPaginada<Patrimonio> registros);

    Arquivo gerarRelatorioMemorandoPDF(RelatorioMemorando relatorio);

    Arquivo gerarRelatorioMemorandoMovimentacaoPDF(RelatorioMemorandoMovimentacao relatorio);
}
