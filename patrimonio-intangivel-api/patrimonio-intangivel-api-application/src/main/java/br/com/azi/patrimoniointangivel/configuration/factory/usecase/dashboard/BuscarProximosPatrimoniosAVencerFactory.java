package br.com.azi.patrimoniointangivel.configuration.factory.usecase.dashboard;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscarproximospatrimoniosavencer.BuscarProximosPatrimoniosAVencerUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscarproximospatrimoniosavencer.BuscarProximosPatrimoniosAVencerUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscarproximospatrimoniosavencer.converter.BuscarProximosPatrimoniosAVencerOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.time.Clock;

@Configuration
public class BuscarProximosPatrimoniosAVencerFactory {

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    @Bean("BuscarProximosPatrimoniosAVencerUseCase")
    @DependsOn({"BuscarProximosPatrimoniosAVencerOutputDataConverter"})
    public BuscarProximosPatrimoniosAVencerUseCase createUseCase(BuscarProximosPatrimoniosAVencerOutputDataConverter outputDataConverter, Clock clock) {
        return new BuscarProximosPatrimoniosAVencerUseCaseImpl(patrimonioDataProvider,
            sistemaDeGestaoAdministrativaIntegration,
            clock,
            outputDataConverter) {
        };
    }

    @Bean("BuscarProximosPatrimoniosAVencerOutputDataConverter")
    public BuscarProximosPatrimoniosAVencerOutputDataConverter createOutputDataConverter() {
        return new BuscarProximosPatrimoniosAVencerOutputDataConverter();
    }
}
