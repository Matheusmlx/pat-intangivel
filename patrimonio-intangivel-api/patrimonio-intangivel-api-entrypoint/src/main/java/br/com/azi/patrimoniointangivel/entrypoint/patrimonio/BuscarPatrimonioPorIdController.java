package br.com.azi.patrimoniointangivel.entrypoint.patrimonio;

import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarporid.BuscarPatrimonioPorIdInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarporid.BuscarPatrimonioPorIdOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarporid.BuscarPatrimonioPorIdUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/{id}")
public class BuscarPatrimonioPorIdController {

    @Autowired
    private BuscarPatrimonioPorIdUseCase useCase;

    @GetMapping
    public BuscarPatrimonioPorIdOutputData buscarTodos(BuscarPatrimonioPorIdInputData inputData) {
        return useCase.executar(inputData);
    }
}
