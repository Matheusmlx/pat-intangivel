package br.com.azi.patrimoniointangivel.configuration.factory.usecase.patrimonio;

import br.com.azi.patrimoniointangivel.configuration.properties.patrimoniointangivel.entity.PatrimonioIntangivelProperties;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarlistagem.BuscarPatrimoniosUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarlistagem.BuscarPatrimoniosUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarlistagem.converter.BuscarPatrimoniosFiltroConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarlistagem.converter.BuscarPatrimoniosOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

@Configuration
public class BuscarPatrimoniosFactory {

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    @Autowired
    public PatrimonioIntangivelProperties patrimonioIntangivelProperties;

    @Bean("BuscarPatrimoniosUseCase")
    @DependsOn({"BuscarPatrimoniosFiltroConverter", "BuscarPatrimoniosOutputDataConverter"})
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BuscarPatrimoniosUseCase createUseCase(BuscarPatrimoniosFiltroConverter filtroConverter, BuscarPatrimoniosOutputDataConverter outputDataConverter) {
        return new BuscarPatrimoniosUseCaseImpl(
            patrimonioDataProvider,
            sistemaDeGestaoAdministrativaIntegration,
            filtroConverter,
            outputDataConverter
        );
    }
    @Bean("BuscarPatrimoniosFiltroConverter")
    public BuscarPatrimoniosFiltroConverter createFiltroConverter() {
        return new BuscarPatrimoniosFiltroConverter();
    }

    @Bean("BuscarPatrimoniosOutputDataConverter")
    public BuscarPatrimoniosOutputDataConverter createOutputDataConverter() {
        return new BuscarPatrimoniosOutputDataConverter();
    }
}
