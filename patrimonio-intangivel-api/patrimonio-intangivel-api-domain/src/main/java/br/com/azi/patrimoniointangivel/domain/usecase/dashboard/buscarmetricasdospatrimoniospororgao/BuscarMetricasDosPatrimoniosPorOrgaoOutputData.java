package br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscarmetricasdospatrimoniospororgao;

import br.com.azi.patrimoniointangivel.domain.entity.MetricasPorOrgao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarMetricasDosPatrimoniosPorOrgaoOutputData {
    private List<MetricasPorOrgao> itens;
}
