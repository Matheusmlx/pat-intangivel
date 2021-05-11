package br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.iniciaamortizacaomanual;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IniciaAmortizacaoManualInputData {
    private Long orgao;
    private String mes;
    private String ano;
}
