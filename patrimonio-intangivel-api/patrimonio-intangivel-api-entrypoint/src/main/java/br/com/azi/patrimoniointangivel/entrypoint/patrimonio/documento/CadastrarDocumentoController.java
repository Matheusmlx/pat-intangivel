package br.com.azi.patrimoniointangivel.entrypoint.patrimonio.documento;


import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.cadastro.CadastrarDocumentoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.cadastro.CadastrarDocumentoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.cadastro.CadastrarDocumentoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/{patrimonioId}/documentos")
public class CadastrarDocumentoController {

    @Autowired
    CadastrarDocumentoUseCase useCase;

    @PostMapping
    public CadastrarDocumentoOutputData executar(@RequestBody CadastrarDocumentoInputData inputData) {
        return useCase.executar(inputData);
    }
}
