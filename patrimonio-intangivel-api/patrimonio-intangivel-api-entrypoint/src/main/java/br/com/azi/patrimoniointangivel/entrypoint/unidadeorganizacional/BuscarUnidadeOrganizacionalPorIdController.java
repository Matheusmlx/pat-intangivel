package br.com.azi.patrimoniointangivel.entrypoint.unidadeorganizacional;

import br.com.azi.patrimoniointangivel.domain.usecase.unidadeorganizacional.buscarfilhos.BuscarUnidadeOrganizacionalPorIdOrgaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.unidadeorganizacional.buscarfilhos.BuscarUnidadeOrganizacionalPorIdOrgaoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.unidadeorganizacional.buscarfilhos.BuscarUnidadeOrganizacionalPorIdOrgaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/unidades-organizacionais/{id}")
public class BuscarUnidadeOrganizacionalPorIdController {
    @Autowired
    private BuscarUnidadeOrganizacionalPorIdOrgaoUseCase usecase;

    @GetMapping
    public BuscarUnidadeOrganizacionalPorIdOrgaoOutputData executar(BuscarUnidadeOrganizacionalPorIdOrgaoInputData inputData) {
        return usecase.executar(inputData);
    }
}
