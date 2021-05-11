package br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.calculaquotasconstantes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuotasConstantesOutputData {

    private BigDecimal valorAnterior;
    private BigDecimal valorPosteiror;
    private BigDecimal valorSubtraido;
    private BigDecimal taxaAplicada;
}
