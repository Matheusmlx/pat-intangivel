package br.com.azi.patrimoniointangivel.configuration.factory.usecase.unidadeorganizacional;

import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.unidadeorganizacional.buscarfilhos.BuscarUnidadeOrganizacionalPorIdOrgaoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.unidadeorganizacional.buscarfilhos.BuscarUnidadeOrganizacionalPorIdOrgaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.unidadeorganizacional.buscarfilhos.converter.BuscarUnidadeOrganizacionalPorIdOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarUnidadeOrganizacionalPorIdFactory {

    @Autowired
    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    @Bean("BuscarUnidadeOrganizacionalPorIdUseCase")
    @DependsOn({"BuscarUnidadeOrganizacionalPorIdOutputDataConverter"})
    public BuscarUnidadeOrganizacionalPorIdOrgaoUseCase createUseCase(BuscarUnidadeOrganizacionalPorIdOutputDataConverter outputDataConverter) {
        return new BuscarUnidadeOrganizacionalPorIdOrgaoUseCaseImpl(
            sistemaDeGestaoAdministrativaIntegration,
            outputDataConverter
        );
    }

    @Bean("BuscarUnidadeOrganizacionalPorIdOutputDataConverter")
    public BuscarUnidadeOrganizacionalPorIdOutputDataConverter createConverter() {
        return new BuscarUnidadeOrganizacionalPorIdOutputDataConverter();
    }
}
