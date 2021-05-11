package br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarinventarioanaliticoxls.entity;

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
public class RelatorioInventarioAnalitico {
    private List<ContaContabil> contasContabeis;

    private BigDecimal valorAquisicao;
    private BigDecimal amortizacaoMensal;
    private BigDecimal amortizacaoAcumulada;
    private BigDecimal valorLiquido;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContaContabil {
        private String nome;
        private int totalDeBens;
        private BigDecimal valorAquisicao;
        private BigDecimal amortizacaoMensal;
        private BigDecimal amortizacaoAcumulada;
        private BigDecimal valorLiquido;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Patrimonio {
        private String numero;
        private String nome;
        private String tipo;
        private String amortizavel;
        private BigDecimal valorAquisicao;
        private BigDecimal amortizacaoMensal;
        private BigDecimal amortizacaoAcumulada;
        private BigDecimal valorLiquido;
    }
}
