package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.sintetico;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GerarInventarioSinteticoInputData {
    private String formato;
    private Long orgao;
    private String mesReferencia;
}
