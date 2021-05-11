package br.com.azi.patrimoniointangivel.entrypoint.patrimonio;

import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.removernaoalterado.RemoverPatrimonioNaoAlteradoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.removernaoalterado.RemoverPatrimonioNaoAlteradoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequestMapping("/patrimonios/{id}/nao-alterado")
public class RemoverPatrimonioNaoAlteradoController {

    @Autowired
    private RemoverPatrimonioNaoAlteradoUseCase usecase;

    @DeleteMapping
    public ResponseEntity executar(RemoverPatrimonioNaoAlteradoInputData inputData) {
        usecase.executar(inputData);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
