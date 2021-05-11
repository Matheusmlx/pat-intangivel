package br.com.azi.patrimoniointangivel.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioAnalitico {
    private Orgao orgao;
    private List<ContaContabilIn> contasContabeis;
    private LocalDateTime dataRelatorio;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContaContabilIn {
        private Long id;
        private List<Patrimonio> patrimonios;
        private int totalDeBens;
        private String codigo;
        private String descricao;
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
    public static class Orgao {
        private String nome;
        private String sigla;
        private BigDecimal totalValorAquisicao;
        private BigDecimal totalAmortizacaoMensal;
        private BigDecimal totalAmortizacaoAcumulada;
        private BigDecimal totalValorLiquido;
        private int totalDeBens;
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
