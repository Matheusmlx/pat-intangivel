package br.com.azi.patrimoniointangivel.entrypoint.patrimonio.documento;

import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.edicao.EditarDocumentoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.edicao.EditarDocumentoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.edicao.EditarDocumentoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/{patrimonioId}/documentos/{id}")
public class EditarDocumentoController {

    @Autowired
    EditarDocumentoUseCase useCase;

    @PutMapping
    public EditarDocumentoOutputData executar(@PathVariable Long id, @RequestBody EditarDocumentoInputData inputData) {
        inputData.setId(id);
        return useCase.executar(inputData);
    }
}
