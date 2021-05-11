package br.com.azi.patrimoniointangivel.configuration.factory.usecase.relatorio.inventario.sintetico;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.LancamentosContabeisDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.RelatorioSinteticoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.UnidadeOrganizacionalDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.sintetico.GerarInventarioSinteticoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.sintetico.GerarInventarioSinteticoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.sintetico.converter.GerarInventarioSinteticoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;


@Configuration
public class GerarInventarioSinteticoFactory {

    @Autowired
    private SistemaDeRelatoriosIntegration sistemaDeRelatoriosIntegration;

    @Autowired
    private LancamentosContabeisDataProvider lancamentosContabeisDataProvider;

    @Autowired
    private RelatorioSinteticoDataProvider relatorioSinteticoDataProvider;

    @Autowired
    private UnidadeOrganizacionalDataProvider unidadeOrganizacionalDataProvider;

    @Bean("GerarInventarioSinteticoUseCase")
    @DependsOn("GerarInventarioSinteticoOutputDataConverter")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public GerarInventarioSinteticoUseCase createUseCase(GerarInventarioSinteticoOutputDataConverter outputDataConverter) {
        return new GerarInventarioSinteticoUseCaseImpl(relatorioSinteticoDataProvider,
            lancamentosContabeisDataProvider,
            sistemaDeRelatoriosIntegration,
            unidadeOrganizacionalDataProvider,
            outputDataConverter);
    }

    @Bean("GerarInventarioSinteticoOutputDataConverter")
    public GerarInventarioSinteticoOutputDataConverter createOutputDataConverter() {
        return new GerarInventarioSinteticoOutputDataConverter();
    }

}
