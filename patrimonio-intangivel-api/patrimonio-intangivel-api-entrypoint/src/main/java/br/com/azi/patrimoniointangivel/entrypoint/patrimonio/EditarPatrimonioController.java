package br.com.azi.patrimoniointangivel.entrypoint.patrimonio;

import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.edicao.EditarPatrimonioInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.edicao.EditarPatrimonioOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.edicao.EditarPatrimonioUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequestMapping("/patrimonios/{id}")
public class EditarPatrimonioController {

    @Autowired
    EditarPatrimonioUseCase useCase;

    @PutMapping
    public EditarPatrimonioOutputData executar(@PathVariable Long id, @RequestBody EditarPatrimonioInputData inputData) {
        inputData.setId(id);
        return useCase.executar(inputData);
    }
}
