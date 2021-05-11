package br.com.azi.patrimoniointangivel.entrypoint.patrimonio;

import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.cadastro.CadastraPatrimonioInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.cadastro.CadastraPatrimonioOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.cadastro.CadastraPatrimonioUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios")
public class CadastraPatrimonioController {

    @Autowired
    CadastraPatrimonioUseCase useCase;

    @PostMapping
    public CadastraPatrimonioOutputData executar(@RequestBody CadastraPatrimonioInputData inputData) {
        return useCase.executar(inputData);
    }

}
