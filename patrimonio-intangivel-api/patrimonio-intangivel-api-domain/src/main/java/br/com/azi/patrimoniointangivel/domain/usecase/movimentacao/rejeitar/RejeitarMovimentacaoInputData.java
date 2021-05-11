package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.rejeitar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RejeitarMovimentacaoInputData {
    private Long id;
}
