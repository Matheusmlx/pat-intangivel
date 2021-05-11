package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.enviar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnviarMovimentacaoInputData {
    private Long id;
}
