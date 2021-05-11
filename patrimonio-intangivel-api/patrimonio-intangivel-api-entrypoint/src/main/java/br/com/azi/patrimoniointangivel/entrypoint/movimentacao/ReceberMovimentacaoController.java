package br.com.azi.patrimoniointangivel.entrypoint.movimentacao;

import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.receber.ReceberMovimentacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.receber.ReceberMovimentacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacao/{id}/receber")
public class ReceberMovimentacaoController {

    @Autowired
    ReceberMovimentacaoUseCase useCase;

    @PutMapping
    public ResponseEntity executar(@PathVariable("id") Long id){
        useCase.executar(new ReceberMovimentacaoInputData(id));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
