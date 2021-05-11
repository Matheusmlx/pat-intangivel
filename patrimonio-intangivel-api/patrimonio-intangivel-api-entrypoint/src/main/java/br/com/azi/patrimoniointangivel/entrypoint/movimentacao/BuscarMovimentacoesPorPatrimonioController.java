package br.com.azi.patrimoniointangivel.entrypoint.movimentacao;

import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporpatrimonio.BuscarMovimentacoesPorPatrimonioInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporpatrimonio.BuscarMovimentacoesPorPatrimonioOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporpatrimonio.BuscarMovimentacoesPorPatrimonioUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacoes-patrimonio/")
public class BuscarMovimentacoesPorPatrimonioController {

    @Autowired
    private BuscarMovimentacoesPorPatrimonioUseCase useCase;

    @GetMapping
    public BuscarMovimentacoesPorPatrimonioOutputData buscarTodos(BuscarMovimentacoesPorPatrimonioInputData inputData){
        return useCase.executar(inputData);
    }

}
