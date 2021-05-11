package br.com.azi.patrimoniointangivel.entrypoint.amortizacao;

import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.iniciaamortizacaomanual.IniciaAmortizacaoManualInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.iniciaamortizacaomanual.IniciaAmortizacaoManualOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.iniciaamortizacaomanual.IniciaAmortizacaoManualUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/amortizacao-manual")
public class AmortizacaoManualController {


        @Autowired
        IniciaAmortizacaoManualUseCase useCase;

        @PostMapping
        public IniciaAmortizacaoManualOutputData executar(@RequestBody IniciaAmortizacaoManualInputData inputData) {
            return useCase.executar(inputData);
        }
}
