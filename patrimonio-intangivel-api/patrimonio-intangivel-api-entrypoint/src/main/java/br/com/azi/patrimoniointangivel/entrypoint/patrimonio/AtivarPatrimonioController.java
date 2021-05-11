package br.com.azi.patrimoniointangivel.entrypoint.patrimonio;

import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.ativar.AtivarPatrimonioInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.ativar.AtivarPatrimonioOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.ativar.AtivarPatrimonioUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequestMapping("/patrimonios/{id}")
public class AtivarPatrimonioController {

    @Autowired
    private AtivarPatrimonioUseCase useCase;

    @PatchMapping
    public AtivarPatrimonioOutputData ativar(AtivarPatrimonioInputData inputData) {
        return useCase.executar(inputData);
    }
}
