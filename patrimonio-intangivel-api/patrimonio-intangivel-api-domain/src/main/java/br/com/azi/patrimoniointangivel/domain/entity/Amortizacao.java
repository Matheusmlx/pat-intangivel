package br.com.azi.patrimoniointangivel.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Amortizacao {

    private Long id;
    private LocalDateTime dataInicial;
    private LocalDateTime dataFinal;
    private BigDecimal valorAnterior;
    private BigDecimal valorPosterior;
    private BigDecimal valorSubtraido;
    private BigDecimal taxaAplicada;
    private Patrimonio patrimonio;
    private ConfigAmortizacao configAmortizacao;
    private UnidadeOrganizacional orgao;
}
