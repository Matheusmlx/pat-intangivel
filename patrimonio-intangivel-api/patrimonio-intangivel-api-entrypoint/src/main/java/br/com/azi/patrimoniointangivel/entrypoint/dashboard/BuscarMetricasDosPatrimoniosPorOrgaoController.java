package br.com.azi.patrimoniointangivel.entrypoint.dashboard;

import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscarmetricasdospatrimoniospororgao.BuscarMetricasDosPatrimoniosPorOrgaoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscarmetricasdospatrimoniospororgao.BuscarMetricasDosPatrimoniosPorOrgaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard/metricas-por-orgao")
public class BuscarMetricasDosPatrimoniosPorOrgaoController {

    @Autowired
    private BuscarMetricasDosPatrimoniosPorOrgaoUseCase useCase;

    @GetMapping
    public BuscarMetricasDosPatrimoniosPorOrgaoOutputData buscar() {
        return useCase.executar();
    }
}
