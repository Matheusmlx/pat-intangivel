package br.com.azi.patrimoniointangivel.entrypoint.configuracao.contacontabil;

import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscar.BuscarContasContabeisInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscar.BuscarContasContabeisOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscar.BuscarContasContabeisUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configuracao/contas-contabeis")
public class BuscarContasContabeisController {

    @Autowired
    private BuscarContasContabeisUseCase usecase;

    @GetMapping
    public BuscarContasContabeisOutputData executar(BuscarContasContabeisInputData inputData) {
        return usecase.executar(inputData);
    }
}
