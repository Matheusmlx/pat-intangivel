package br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscartotalizadores.converter;

import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscartotalizadores.BuscarTotalizadoresOutputData;

public class BuscarTotalizadoresOutputDataConverter {

    public BuscarTotalizadoresOutputData to(Long totalDeRegistros, Long emElaboracao, Long ativos) {
        return BuscarTotalizadoresOutputData
            .builder()
            .totalDeRegistros(totalDeRegistros)
            .emElaboracao(emElaboracao)
            .ativos(ativos)
            .build();
    }
}
