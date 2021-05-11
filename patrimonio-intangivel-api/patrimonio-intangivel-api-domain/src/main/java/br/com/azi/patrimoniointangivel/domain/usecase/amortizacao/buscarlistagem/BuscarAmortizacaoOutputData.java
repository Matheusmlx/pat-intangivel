package br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.buscarlistagem;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class BuscarAmortizacaoOutputData {

    List<Amortizacao> items;

    @Data
    @NoArgsConstructor
    public static class Amortizacao {
        private Long id;
        private LocalDateTime dataInicial;
        private LocalDateTime dataFinal;
        private BigDecimal valorAnterior;
        private BigDecimal valorPosterior;
        private BigDecimal valorSubtraido;
        private BigDecimal taxaAplicada;
        private String orgaoSigla;
    }
}
