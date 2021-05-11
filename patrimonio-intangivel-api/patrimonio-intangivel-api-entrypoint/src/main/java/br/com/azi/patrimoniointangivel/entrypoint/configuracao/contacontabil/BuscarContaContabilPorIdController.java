package br.com.azi.patrimoniointangivel.entrypoint.configuracao.contacontabil;

import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscarporid.BuscarContaContabilPorIdInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscarporid.BuscarContaContabilPorIdOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscarporid.BuscarContaContabilPorIdUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configuracao/contas-contabeis/{id}")
public class BuscarContaContabilPorIdController {

    @Autowired
    private BuscarContaContabilPorIdUseCase usecase;

    @GetMapping
    public BuscarContaContabilPorIdOutputData executar(@RequestBody BuscarContaContabilPorIdInputData inputData) {
        return usecase.executar(inputData);
    }
}
