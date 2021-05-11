package br.com.azi.patrimoniointangivel.entrypoint.movimentacao.documento;

import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.buscar.BuscarDocumentoMovimentacaoIntputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.buscar.BuscarDocumentoMovimentacaoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.buscar.BuscarDocumentoMovimentacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacao/{movimentacaoId}/documentos")
public class BuscarDocumentoMovimentacaoController {

    @Autowired
    private BuscarDocumentoMovimentacaoUseCase useCase;

    @GetMapping
    public BuscarDocumentoMovimentacaoOutputData buscarTodos(BuscarDocumentoMovimentacaoIntputData intputData) {
        return useCase.executar(intputData);
    }
}
