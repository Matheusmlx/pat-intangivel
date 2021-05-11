package br.com.azi.patrimoniointangivel.entrypoint.movimentacao;

import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.cadastro.CriarMovimentacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.cadastro.CriarMovimentacaoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.cadastro.CriarMovimentacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacao")
public class CriarMovimentacaoController {

    @Autowired
    CriarMovimentacaoUseCase useCase;

    @PostMapping
    public CriarMovimentacaoOutputData executar(@RequestBody CriarMovimentacaoInputData inputData){
        return useCase.executar(inputData);
    }
}
