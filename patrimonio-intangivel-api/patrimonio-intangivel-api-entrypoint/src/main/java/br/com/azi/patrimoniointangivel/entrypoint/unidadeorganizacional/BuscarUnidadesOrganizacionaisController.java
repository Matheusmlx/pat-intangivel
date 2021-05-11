package br.com.azi.patrimoniointangivel.entrypoint.unidadeorganizacional;


import br.com.azi.patrimoniointangivel.domain.usecase.unidadeorganizacional.buscarorgao.BuscarUnidadesOrganizacionaisOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.unidadeorganizacional.buscarorgao.BuscarUnidadesOrganizacionaisUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/unidades-organizacionais")
public class BuscarUnidadesOrganizacionaisController {

    @Autowired
    private BuscarUnidadesOrganizacionaisUseCase usecase;

    @GetMapping
    public BuscarUnidadesOrganizacionaisOutputData executar() {
        return usecase.executar();
    }
}
