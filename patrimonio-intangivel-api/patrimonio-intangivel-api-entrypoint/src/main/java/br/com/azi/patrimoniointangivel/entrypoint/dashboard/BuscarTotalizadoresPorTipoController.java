package br.com.azi.patrimoniointangivel.entrypoint.dashboard;

import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscartotalizadoresportipo.BuscarTotalizadoresPorTipoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscartotalizadoresportipo.BuscarTotalizadoresPorTipoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard/totalizadores-por-tipo")
public class BuscarTotalizadoresPorTipoController {

    @Autowired
    private BuscarTotalizadoresPorTipoUseCase useCase;

    @GetMapping
    public BuscarTotalizadoresPorTipoOutputData buscar() {
        return useCase.executar();
    }
}
