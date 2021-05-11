package br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.salvadadosamortizacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalvaDadosAmortizacaoInputData {
    private Long patrimonio;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFinal;
}
