package br.com.azi.patrimoniointangivel.entrypoint.movimentacao;

import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarlistagem.BuscarMovimentacoesInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarlistagem.BuscarMovimentacoesOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarlistagem.BuscarMovimentacoesUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacoes")
public class BuscarMovimentacoesController {

    @Autowired
    private BuscarMovimentacoesUseCase useCase;

    @GetMapping
    public BuscarMovimentacoesOutputData buscarMovimentacoesOutputData(BuscarMovimentacoesInputData inputData){
        return useCase.executar(inputData);
    }
}
