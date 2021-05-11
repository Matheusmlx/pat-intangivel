package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.gerarrelatoriomovimentacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GerarRelatorioMemorandoMovimentacaoPDFInputData {
    private Long id;
}
