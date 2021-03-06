package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.rotulospersonalizados.converter;

import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.rotulospersonalizados.BuscarRotulosPersonalizadosOutputData;

import java.util.Map;

public class RotulosPersonalizadosConverter {

    public BuscarRotulosPersonalizadosOutputData to(Map<String, Object> rotulosPersonalizados) {
        return BuscarRotulosPersonalizadosOutputData.builder()
            .rotulosPersonalizados(rotulosPersonalizados)
            .build();
    }
}
