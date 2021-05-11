package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.patrimonio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GerarNumeroPatrimonioOutputData {
    private String numero;
}
