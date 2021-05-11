package br.com.azi.patrimoniointangivel.entrypoint.tipodocumento;

import br.com.azi.patrimoniointangivel.domain.usecase.tipodocumento.buscardocumentos.BuscarTipoDocumentosOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.tipodocumento.buscardocumentos.BuscarTipoDocumentosUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tiposdocumentos")
public class BuscarTipoDocumentosController {

    @Autowired
    private BuscarTipoDocumentosUseCase useCase;

    @GetMapping
    public BuscarTipoDocumentosOutputData buscarTodos() {
        return useCase.executar();
    }
}
