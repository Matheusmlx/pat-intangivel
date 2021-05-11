package br.com.azi.patrimoniointangivel.entrypoint.movimentacao;

import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporid.BuscarMovimentacaoPorIdInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporid.BuscarMovimentacaoPorIdOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporid.BuscarMovimentacaoPorIdUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacao/{id}")
public class BuscarMovimentacaoPorIdController {

    @Autowired
    private BuscarMovimentacaoPorIdUseCase useCase;

    @GetMapping
    public BuscarMovimentacaoPorIdOutputData buscarPorId(BuscarMovimentacaoPorIdInputData idInputData){
        return useCase.executar(idInputData);
    }
}
