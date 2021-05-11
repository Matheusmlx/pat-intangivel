package br.com.azi.patrimoniointangivel.entrypoint.configuracao.parametro;

import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.parametro.buscar.BuscarParametrosOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.parametro.buscar.BuscarParametrosUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configuracao/parametros")
public class BuscarParametrosController {

    @Autowired
    private BuscarParametrosUseCase usecase;

    @GetMapping
    public BuscarParametrosOutputData executar() {
        return usecase.executar();
    }
}
