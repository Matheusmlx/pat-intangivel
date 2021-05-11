package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.memorando;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GerarNumeroMemorandoOutputData {
    private String numeroMemorando;
}
