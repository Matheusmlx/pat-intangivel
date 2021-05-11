package br.com.azi.patrimoniointangivel.entrypoint.patrimonio;

import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.visualizacao.VisualizarPatrimonioInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.visualizacao.VisualizarPatrimonioOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.visualizacao.VisualizarPatrimonioUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/{id}/visualizacao-completa")
public class VisualizarPatrimonioController {

    @Autowired
    private VisualizarPatrimonioUseCase useCase;

    @GetMapping
    public VisualizarPatrimonioOutputData buscar(VisualizarPatrimonioInputData inputData) {
        return useCase.executar(inputData);
    }
}
