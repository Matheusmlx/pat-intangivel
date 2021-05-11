package br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscarmetricasdospatrimoniospororgao.converter;

import br.com.azi.patrimoniointangivel.domain.entity.MetricasPorOrgao;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscarmetricasdospatrimoniospororgao.BuscarMetricasDosPatrimoniosPorOrgaoOutputData;

import java.util.List;

public class BuscarMetricasDosPatrimoniosPorOrgaoDataConverter {

    public BuscarMetricasDosPatrimoniosPorOrgaoOutputData to(List<MetricasPorOrgao> itens) {
        return BuscarMetricasDosPatrimoniosPorOrgaoOutputData.builder()
            .itens(itens).build();
    }
}
