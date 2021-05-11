package br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarinventarioanaliticopdf.entity;

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
    private BigDecimal totalValorAquisicao;
    private BigDecimal totalAmortizacaoMensal;
    private BigDecimal totalAmortizacaoAcumulada;
    private BigDecimal totalValorLiquido;
    private String totalDeBens;
    private String dataRelatorio;
    private String nomeOrgao;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContaContabil {
        private Long id;
        private List<Patrimonio> patrimonios;
        private String codigo;
        private String descricao;
        private int totalDeBens;
        private BigDecimal valorAquisicao;
        private BigDecimal amortizacaoMensal;
        private BigDecimal amortizacaoAcumulada;
        private BigDecimal valorLiquido;
        private Boolean amortizavel;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Patrimonio {
        private Long id;
        private String numero;
        private String nome;
        private String tipo;
        private Boolean amortizavel;
        private BigDecimal valorLiquido;
        private BigDecimal valorAquisicao;
        private BigDecimal valorAmortizadoMensal;
        private BigDecimal valorAmortizadoAcumulado;
    }
}
