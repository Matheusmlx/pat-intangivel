package br.com.azi.patrimoniointangivel.entrypoint.movimentacao;

import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.enviar.EnviarMovimentacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.enviar.EnviarMovimentacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@Transactional
@RequestMapping("/movimentacao/{id}/enviar")
public class EnviarMovimentacaoController {

    @Autowired
    EnviarMovimentacaoUseCase useCase;

    @PutMapping
    public ResponseEntity executar(@PathVariable("id") Long id){
        useCase.executar(new EnviarMovimentacaoInputData(id));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
