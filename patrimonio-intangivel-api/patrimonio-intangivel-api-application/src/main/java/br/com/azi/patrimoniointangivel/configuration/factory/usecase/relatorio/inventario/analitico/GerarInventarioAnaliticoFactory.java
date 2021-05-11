package br.com.azi.patrimoniointangivel.configuration.factory.usecase.relatorio.inventario.analitico;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.AmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.LancamentosContabeisDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.analitico.GerarInventarioAnaliticoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.analitico.converter.GerarInventarioAnaliticoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

@Configuration
public class GerarInventarioAnaliticoFactory {

    @Autowired
    private SistemaDeRelatoriosIntegration sistemaDeRelatoriosIntegration;

    @Autowired
    private AmortizacaoDataProvider amortizacaoDataProvider;

    @Autowired
    private ConfigContaContabilDataProvider configContaContabilDataProvider;

    @Autowired
    private LancamentosContabeisDataProvider lancamentosContabeisDataProvider;



    @Bean("GerarRelatorioAnaliticoPDFUseCase")
    @DependsOn("GerarInventarioAnaliticoOutputDataConverter")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public GerarInventarioAnaliticoUseCaseImpl createUseCase(GerarInventarioAnaliticoOutputDataConverter outputDataConverter){
        return new GerarInventarioAnaliticoUseCaseImpl(
            sistemaDeRelatoriosIntegration,
            amortizacaoDataProvider,
            configContaContabilDataProvider,
            lancamentosContabeisDataProvider,
            outputDataConverter);
    }

    @Bean("GerarInventarioAnaliticoOutputDataConverter")
    public GerarInventarioAnaliticoOutputDataConverter createOutputDataConverter(){
        return new GerarInventarioAnaliticoOutputDataConverter();
    }
}
