package br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarinventariosinteticopdf.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioInventarioSintetico {
    private List<ContaContabil> contasContabeis;

    private Long totalOrgaos;
    private BigDecimal totalValorAquisicao;
    private BigDecimal totalAmortizacaoMensal;
    private BigDecimal totalAmortizacaoAcumulada;
    private BigDecimal totalValorLiquido;
    private String dataRelatorio;
    private String nomeOrgao;
    private Boolean temContaContabilAmortizavel = false;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContaContabil {
        private String nome;
        private String tipo;
        private Long totalDeBens;
        private BigDecimal valorAquisicao;
        private BigDecimal amortizacaoMensal;
        private BigDecimal amortizacaoAcumulada;
        private BigDecimal valorLiquido;
    }

}
