package br.com.azi.patrimoniointangivel.domain.usecase.fusohorario.buscar.converter;

import br.com.azi.patrimoniointangivel.domain.usecase.fusohorario.buscar.BuscarFusoHorarioOutputData;

public class BuscarFusoHorarioOutputDataConverter {

    public BuscarFusoHorarioOutputData to(String from) {
        return BuscarFusoHorarioOutputData.builder().fusoHorario(from).build();
    }
}
