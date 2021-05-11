package br.com.azi.patrimoniointangivel.configuration.factory.usecase.dashboard;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscartotalizadoresportipo.BuscarTotalizadoresPorTipoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscartotalizadoresportipo.BuscarTotalizadoresPorTipoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscartotalizadoresportipo.converter.BuscarTotalizadoresPorTipoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

@Configuration
public class BuscarTotalizadoresPorTipoFactory {

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    @Bean("BuscarTotalizadoresPorTipoUseCase")
    @DependsOn({"BuscarTotalizadoresPorTipoOutputDataConverter"})
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BuscarTotalizadoresPorTipoUseCase createUseCase(BuscarTotalizadoresPorTipoOutputDataConverter outputDataConverter) {
        return new BuscarTotalizadoresPorTipoUseCaseImpl(patrimonioDataProvider, sistemaDeGestaoAdministrativaIntegration, outputDataConverter) {
        };
    }

    @Bean("BuscarTotalizadoresPorTipoOutputDataConverter")
    public BuscarTotalizadoresPorTipoOutputDataConverter createOutputDataConverter() {
        return new BuscarTotalizadoresPorTipoOutputDataConverter();
    }
}
