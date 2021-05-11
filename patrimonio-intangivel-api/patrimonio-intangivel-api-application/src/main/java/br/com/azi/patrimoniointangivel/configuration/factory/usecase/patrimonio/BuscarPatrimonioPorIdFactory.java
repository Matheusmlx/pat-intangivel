package br.com.azi.patrimoniointangivel.configuration.factory.usecase.patrimonio;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarporid.BuscarPatrimonioPorIdUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarporid.BuscarPatrimonioPorIdUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarporid.converter.BuscarPatrimonioPorIdOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

@Configuration
public class BuscarPatrimonioPorIdFactory {

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    @Bean("BuscarPatrimonioPorIdUseCase")
    @DependsOn({"BuscarPatrimonioPorIdOutputDataConverter"})
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BuscarPatrimonioPorIdUseCase createUseCase(BuscarPatrimonioPorIdOutputDataConverter outputDataConverter) {
        return new BuscarPatrimonioPorIdUseCaseImpl(
            patrimonioDataProvider,
            sistemaDeGestaoAdministrativaIntegration,
            outputDataConverter
        );
    }

    @Bean("BuscarPatrimonioPorIdOutputDataConverter")
    public BuscarPatrimonioPorIdOutputDataConverter createOutputDataConverter() {
        return new BuscarPatrimonioPorIdOutputDataConverter();
    }
}
