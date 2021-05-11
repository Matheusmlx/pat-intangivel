package br.com.azi.patrimoniointangivel.entrypoint.movimentacao.documento;

import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.deletar.DeletarDocumentoMovimentacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.deletar.DeletarDocumentoMovimentacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacao/{movimentacaoId}/documentos/{id}")
public class DeletarDocumentoMovimentacaoController {

    @Autowired
    private DeletarDocumentoMovimentacaoUseCase usecase;

    @DeleteMapping
    public ResponseEntity executar(DeletarDocumentoMovimentacaoInputData inputData) {
        usecase.executar(inputData);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
