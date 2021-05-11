package br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.calculaquotasconstantes;

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
public class QuotasConstantesInputData {

    private BigDecimal valorAquisicao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFinal;
    private BigDecimal percentualResidual;
    private BigDecimal taxa;
    private BigDecimal valorLiquido;
    private Boolean ultimoMesVidaUtil;
}
