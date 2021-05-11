package br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscarproximospatrimoniosavencer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarProximosPatrimoniosAVencerInputData {
    private Long quantidadeDeRegistros;
}
