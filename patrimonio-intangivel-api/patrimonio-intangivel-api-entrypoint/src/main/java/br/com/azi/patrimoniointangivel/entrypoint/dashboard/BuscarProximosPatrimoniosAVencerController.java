package br.com.azi.patrimoniointangivel.entrypoint.dashboard;

import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscarproximospatrimoniosavencer.BuscarProximosPatrimoniosAVencerInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscarproximospatrimoniosavencer.BuscarProximosPatrimoniosAVencerOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscarproximospatrimoniosavencer.BuscarProximosPatrimoniosAVencerUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard/licenca/proximas-vencer")
public class BuscarProximosPatrimoniosAVencerController {

    @Autowired
    private BuscarProximosPatrimoniosAVencerUseCase useCase;

    @GetMapping
    public BuscarProximosPatrimoniosAVencerOutputData buscar(@RequestParam(name = "quantidade") Long quantidade) {

        return useCase.executar(new BuscarProximosPatrimoniosAVencerInputData(quantidade));
    }
}
