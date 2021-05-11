package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.calculadadosamortizacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CalculaConfigAmortizacaoInputData {

    private int vidaUtil;

}
