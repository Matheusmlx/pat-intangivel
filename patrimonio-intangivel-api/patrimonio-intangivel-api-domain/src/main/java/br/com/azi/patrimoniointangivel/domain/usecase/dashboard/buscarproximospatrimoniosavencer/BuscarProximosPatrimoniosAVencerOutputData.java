package br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscarproximospatrimoniosavencer;

import br.com.azi.patrimoniointangivel.domain.entity.PatrimonioComDiasParaVencer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarProximosPatrimoniosAVencerOutputData {
    private List<PatrimonioComDiasParaVencer> itens;
}
