package br.com.azi.patrimoniointangivel.domain.entity;

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
public class RegistrosInventarioSintetico {

    private List<ContaContabil> contasContabeis;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContaContabil {
        private String codigo;
        private String descricao;
        private int totalBens;
        private BigDecimal valorAmortizado;
        private BigDecimal valorLiquido;
        private BigDecimal valorAquisicao;
        private BigDecimal amortizacaoMensal;
    }

}
