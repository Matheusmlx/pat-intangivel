package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.cadastro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriarMovimentacaoInputData {
    private String tipo;
    private Long idPatrimonio;
}
