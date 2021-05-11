package br.com.azi.patrimoniointangivel.entrypoint.movimentacao;

import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarPatrimonioPorMovimentacao.BuscarPatrimonioPorMovimentacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarPatrimonioPorMovimentacao.BuscarPatrimonioPorMovimentacaoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarPatrimonioPorMovimentacao.BuscarPatrimonioPorMovimentacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacao/patrimonio/{id}")
public class BuscarPatrimonioPorMovimentacaoController {

    @Autowired
    private BuscarPatrimonioPorMovimentacaoUseCase useCase;

    @GetMapping
    public BuscarPatrimonioPorMovimentacaoOutputData buscarPatrimonioPorMovimentacao(BuscarPatrimonioPorMovimentacaoInputData inputData) {
        return useCase.executar(inputData);
    }
}
