package br.com.azi.patrimoniointangivel.entrypoint.movimentacao.documento;

import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.cadastro.CriarDocumentoMovimentacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.cadastro.CriarDocumentoMovimentacaoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.cadastro.CriarDocumentoMovimentacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacao/{movimentacaoId}/documentos")
public class CriarDocumentoMovimentacaoController {

    @Autowired
    CriarDocumentoMovimentacaoUseCase useCase;

    @PostMapping
    public CriarDocumentoMovimentacaoOutputData executar(@RequestBody CriarDocumentoMovimentacaoInputData inputData) {
        return useCase.executar(inputData);
    }
}
