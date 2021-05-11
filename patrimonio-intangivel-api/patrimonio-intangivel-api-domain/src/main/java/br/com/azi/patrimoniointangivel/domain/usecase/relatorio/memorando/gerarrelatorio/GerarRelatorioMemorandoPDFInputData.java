package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.gerarrelatorio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GerarRelatorioMemorandoPDFInputData {
    private Long patrimonioId;
}
