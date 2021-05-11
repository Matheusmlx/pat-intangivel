package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporpatrimonio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarMovimentacoesPorPatrimonioInputData {
    private Long idPatrimonio;
    private Long tamanho;
}
