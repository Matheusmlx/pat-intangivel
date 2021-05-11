package br.com.azi.patrimoniointangivel.entrypoint.patrimonio.documento;


import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.deletar.DeletarDocumentoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.deletar.DeletarDocumentoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/{patrimonioId}/documentos/{id}")
public class RemoverDocumentoController {

    @Autowired
    private DeletarDocumentoUseCase usecase;

    @DeleteMapping
    public ResponseEntity executar(DeletarDocumentoInputData inputData) {
        usecase.executar(inputData);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
