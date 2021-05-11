package br.com.azi.patrimoniointangivel.entrypoint.movimentacao.documento;

import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.editar.EditarDocumentoMovimentacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.editar.EditarDocumentoMovimentacaoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.editar.EditarDocumentoMovimentacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacao/{movimentacaoId}/documentos/{id}")
public class EditarDocumentoMovimentacaoController {

    @Autowired
    EditarDocumentoMovimentacaoUseCase useCase;

    @PutMapping
    public EditarDocumentoMovimentacaoOutputData executar(@PathVariable Long id, @RequestBody EditarDocumentoMovimentacaoInputData inputData) {
        inputData.setId(id);
        return useCase.executar(inputData);
    }
}
