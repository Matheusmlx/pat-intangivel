package br.com.azi.patrimoniointangivel.entrypoint.configuracao.contacontabil.dadosamortizacao;

import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.editar.EditarContaContabilInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.editar.EditarContaContabilOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.editar.EditarContaContabilUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequestMapping("/configuracao/contas-contabeis/{contaContabilId}/config-amortizacao/{configAmortizacaoId}")
public class EditarConfigContaContabilController {

    @Autowired
    private EditarContaContabilUseCase usecase;

    @PostMapping
    public EditarContaContabilOutputData executar(@PathVariable Long contaContabilId, @PathVariable Long configAmortizacaoId, @RequestBody EditarContaContabilInputData inputData) {
        inputData.setContaContabil(contaContabilId);
        inputData.setId(configAmortizacaoId);
        return usecase.executar(inputData);
    }
}
