package br.com.azi.patrimoniointangivel.configuration.factory.usecase.dashboard;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscarmetricasdospatrimoniospororgao.BuscarMetricasDosPatrimoniosPorOrgaoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscarmetricasdospatrimoniospororgao.BuscarMetricasDosPatrimoniosPorOrgaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscarmetricasdospatrimoniospororgao.converter.BuscarMetricasDosPatrimoniosPorOrgaoDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

@Configuration
public class BuscarMetricasDosPatrimoniosPorOrgaoFactory {

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    @Bean("BuscarMetricasDosPatrimoniosPorOrgaoUseCase")
    @DependsOn({"BuscarMetricasDosPatrimoniosPorOrgaoOutputDataConverter"})
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BuscarMetricasDosPatrimoniosPorOrgaoUseCase createUseCase(BuscarMetricasDosPatrimoniosPorOrgaoDataConverter outputDataConverter) {
        return new BuscarMetricasDosPatrimoniosPorOrgaoUseCaseImpl(patrimonioDataProvider, sistemaDeGestaoAdministrativaIntegration, outputDataConverter) {
        };
    }

    @Bean("BuscarMetricasDosPatrimoniosPorOrgaoOutputDataConverter")
    public BuscarMetricasDosPatrimoniosPorOrgaoDataConverter createOutputDataConverter() {
        return new BuscarMetricasDosPatrimoniosPorOrgaoDataConverter();
    }
}
