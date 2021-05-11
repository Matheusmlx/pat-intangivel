package br.com.azi.patrimoniointangivel.entrypoint.movimentacao;

import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.edicao.EditarMovimentacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.edicao.EditarMovimentacaoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.edicao.EditarMovimentacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Transactional
@RestController
@RequestMapping("/movimentacao/{id}")
public class EditarMovimentacaoController {

    @Autowired
    EditarMovimentacaoUseCase useCase;

    @PutMapping
    public EditarMovimentacaoOutputData executar(@PathVariable Long id, @RequestBody EditarMovimentacaoInputData inputData){
        inputData.setId(id);
        return useCase.executar(inputData);
    }
}
