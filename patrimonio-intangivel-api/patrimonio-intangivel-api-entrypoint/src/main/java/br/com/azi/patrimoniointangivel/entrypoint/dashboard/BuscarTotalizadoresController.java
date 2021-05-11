package br.com.azi.patrimoniointangivel.entrypoint.dashboard;

import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscartotalizadores.BuscarTotalizadoresOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscartotalizadores.BuscarTotalizadoresUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard/totalizadores")
public class BuscarTotalizadoresController {

    @Autowired
    private BuscarTotalizadoresUseCase useCase;

    @GetMapping
    public BuscarTotalizadoresOutputData buscar() {
        return useCase.executar();
    }
}
