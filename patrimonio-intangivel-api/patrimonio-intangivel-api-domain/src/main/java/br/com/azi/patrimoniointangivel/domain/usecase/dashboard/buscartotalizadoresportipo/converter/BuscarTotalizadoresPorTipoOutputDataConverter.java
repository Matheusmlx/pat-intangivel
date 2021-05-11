package br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscartotalizadoresportipo.converter;

import br.com.azi.patrimoniointangivel.domain.entity.PatrimoniosPorTipo;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscartotalizadoresportipo.BuscarTotalizadoresPorTipoOutputData;

import java.util.List;

public class BuscarTotalizadoresPorTipoOutputDataConverter {
    public BuscarTotalizadoresPorTipoOutputData to(List<PatrimoniosPorTipo> patrimoniosPorTipos) {

        return BuscarTotalizadoresPorTipoOutputData.builder()
            .itens(patrimoniosPorTipos).build();
    }
}
