package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.historicomemorando;

import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GerarHistoricoMemorandoInputData {
    private String numeroMemorando;
    private Arquivo arquivo;
}
