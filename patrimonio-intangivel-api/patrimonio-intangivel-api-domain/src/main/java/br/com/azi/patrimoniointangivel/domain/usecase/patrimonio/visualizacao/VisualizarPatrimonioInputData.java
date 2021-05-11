package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.visualizacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VisualizarPatrimonioInputData {
    private Long id;
}
