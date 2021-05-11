package br.com.azi.patrimoniointangivel.entrypoint.amortizacao;

import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.buscarlistagem.BuscarAmortizacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.buscarlistagem.BuscarAmortizacaoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.buscarlistagem.BuscarAmortizacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/amortizacoes/{id}")
public class BuscarAmortizacaoController {

    @Autowired
    private BuscarAmortizacaoUseCase useCase;

    @GetMapping
    public BuscarAmortizacaoOutputData buscar(BuscarAmortizacaoInputData inputData) {
        return useCase.executar(inputData);
    }
}
