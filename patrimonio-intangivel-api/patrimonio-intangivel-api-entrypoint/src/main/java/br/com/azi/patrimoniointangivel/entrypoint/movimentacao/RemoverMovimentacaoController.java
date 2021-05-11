package br.com.azi.patrimoniointangivel.entrypoint.movimentacao;

import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.remover.RemoverMovimentacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.remover.RemoverMovimentacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@Transactional
@RestController
@RequestMapping("/movimentacao/{id}")
public class RemoverMovimentacaoController {

    @Autowired
    private RemoverMovimentacaoUseCase useCase;

    @DeleteMapping
    public ResponseEntity executar(RemoverMovimentacaoInputData inputData){
        useCase.executar(inputData);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
