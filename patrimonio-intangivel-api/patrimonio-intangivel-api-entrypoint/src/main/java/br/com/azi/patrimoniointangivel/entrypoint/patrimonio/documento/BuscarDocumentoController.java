package br.com.azi.patrimoniointangivel.entrypoint.patrimonio.documento;

import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.buscar.BuscarDocumentoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.buscar.BuscarDocumentoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.buscar.BuscarDocumentoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/{patrimonioId}/documentos")
public class BuscarDocumentoController {

    @Autowired
    private BuscarDocumentoUseCase useCase;

    @GetMapping
    public BuscarDocumentoOutputData buscarTodos(BuscarDocumentoInputData inputData) {
        return useCase.executar(inputData);
    }
}
