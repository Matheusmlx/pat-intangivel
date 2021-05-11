package br.com.azi.patrimoniointangivel.configuration.factory.usecase.patrimonio;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.AmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.visualizacao.VisualizarPatrimonioUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.visualizacao.VisualizarPatrimonioUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.visualizacao.converter.VisualizarPatrimonioOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

@Configuration
public class VisualizarPatrimonioFactory {

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    @Autowired
    private AmortizacaoDataProvider amortizacaoDataProvider;

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Bean("VisualizarPatrimonioUseCase")
    @DependsOn({"VisualizarPatrimonioOutputDataConverter"})
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public VisualizarPatrimonioUseCase createUseCase(VisualizarPatrimonioOutputDataConverter outputDataConverter) {
        return new VisualizarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            amortizacaoDataProvider,
            sistemaDeGestaoAdministrativaIntegration,
            outputDataConverter,
            movimentacaoDataProvider
        );
    }

    @Bean("VisualizarPatrimonioOutputDataConverter")
    public VisualizarPatrimonioOutputDataConverter createOutputDataConverter() {
        return new VisualizarPatrimonioOutputDataConverter(amortizacaoDataProvider);
    }
}
