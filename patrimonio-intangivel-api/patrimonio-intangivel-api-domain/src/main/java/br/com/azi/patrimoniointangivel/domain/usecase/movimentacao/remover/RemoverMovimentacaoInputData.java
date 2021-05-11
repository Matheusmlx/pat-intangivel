package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.remover;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemoverMovimentacaoInputData {
    private Long id;
}
