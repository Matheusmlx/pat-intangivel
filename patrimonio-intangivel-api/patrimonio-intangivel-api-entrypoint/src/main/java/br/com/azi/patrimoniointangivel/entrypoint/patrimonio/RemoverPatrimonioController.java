package br.com.azi.patrimoniointangivel.entrypoint.patrimonio;

import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.remocao.RemoverPatrimonioInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.remocao.RemoverPatrimonioUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequestMapping("/patrimonios/{id}")
public class RemoverPatrimonioController {

    @Autowired
    private RemoverPatrimonioUseCase usecase;

    @DeleteMapping
    public ResponseEntity executar(RemoverPatrimonioInputData inputData) {
        usecase.executar(inputData);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
