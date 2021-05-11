package br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscarproximospatrimoniosavencer.converter;

import br.com.azi.patrimoniointangivel.domain.entity.PatrimonioComDiasParaVencer;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscarproximospatrimoniosavencer.BuscarProximosPatrimoniosAVencerOutputData;

import java.util.List;

public class BuscarProximosPatrimoniosAVencerOutputDataConverter {

    public BuscarProximosPatrimoniosAVencerOutputData to(List<PatrimonioComDiasParaVencer> itens) {
        return BuscarProximosPatrimoniosAVencerOutputData.builder()
            .itens(itens).build();
    }
}
