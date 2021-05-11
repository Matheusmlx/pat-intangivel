package br.com.azi.patrimoniointangivel.entrypoint.patrimonio;

import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarlistagem.BuscarPatrimoniosInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarlistagem.BuscarPatrimoniosOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarlistagem.BuscarPatrimoniosUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios")
public class BuscarPatrimoniosController {

    @Autowired
    private BuscarPatrimoniosUseCase useCase;

    @GetMapping
    public BuscarPatrimoniosOutputData buscarTodos(BuscarPatrimoniosInputData inputData) {
        return useCase.executar(inputData);
    }
}
