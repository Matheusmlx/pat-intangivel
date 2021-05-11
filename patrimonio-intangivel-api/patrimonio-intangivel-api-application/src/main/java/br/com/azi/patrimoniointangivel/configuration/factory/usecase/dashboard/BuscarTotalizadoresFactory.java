package br.com.azi.patrimoniointangivel.configuration.factory.usecase.dashboard;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscartotalizadores.BuscarTotalizadoresUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscartotalizadores.BuscarTotalizadoresUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscartotalizadores.converter.BuscarTotalizadoresOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

@Configuration
public class BuscarTotalizadoresFactory {

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    @Bean("BuscarTotalizadoresUseCase")
    @DependsOn({"BuscarTotalizadoresOutputDataConverter"})
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BuscarTotalizadoresUseCase createUseCase(BuscarTotalizadoresOutputDataConverter buscarTotalizadoresOutputDataConverter) {
        return new BuscarTotalizadoresUseCaseImpl(patrimonioDataProvider, sistemaDeGestaoAdministrativaIntegration, buscarTotalizadoresOutputDataConverter) {
        };
    }

    @Bean("BuscarTotalizadoresOutputDataConverter")
    public BuscarTotalizadoresOutputDataConverter createOutputDataConverter() {
        return new BuscarTotalizadoresOutputDataConverter();
    }
}
