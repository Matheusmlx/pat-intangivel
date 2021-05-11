package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.analitico;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GerarInventarioAnaliticoInputData {
    private String formato;
    private Long orgao;
    private String mesReferencia;
}
