package br.com.azi.patrimoniointangivel.entrypoint.patrimonio;

import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.desativar.DesativarPatrimonioInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.desativar.DesativarPatrimonioOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.desativar.DesativarPatrimonioUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequestMapping("/patrimonios/{id}/desativar")
public class DesativarPatrimonioController {

    @Autowired
    private DesativarPatrimonioUseCase useCase;

    @PatchMapping
    public DesativarPatrimonioOutputData ativar(DesativarPatrimonioInputData inputData) {
        return useCase.executar(inputData);
    }
}
