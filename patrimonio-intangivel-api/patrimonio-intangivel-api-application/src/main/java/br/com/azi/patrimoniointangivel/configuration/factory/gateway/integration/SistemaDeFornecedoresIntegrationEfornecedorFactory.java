package br.com.azi.patrimoniointangivel.configuration.factory.gateway.integration;

import br.com.azi.patrimoniointangivel.configuration.properties.patrimoniointangivel.entity.PatrimonioIntangivelProperties;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeFornecedoresIntegration;
import br.com.azi.patrimoniointangivel.gateway.integration.efornecedor.SistemaDeFornecedoresIntegrationHalImpl;
import br.com.azi.patrimoniointangivel.gateway.integration.efornecedor.entity.EfornecedorIntegrationProperties;
import br.com.azi.patrimoniointangivel.gateway.integration.efornecedor.usecase.buscarporfiltro.BuscarFornecedoresPorFiltroIntegrationUseCase;
import br.com.azi.patrimoniointangivel.gateway.integration.efornecedor.usecase.buscarporid.BuscarFornecedorPorIdIntegrationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@RefreshScope
@Configuration
@ConditionalOnProperty(prefix = "az.patrimonio-config.integration", name = "sistema-de-fornecedores", havingValue = "efornecedor", matchIfMissing = true)
public class SistemaDeFornecedoresIntegrationEfornecedorFactory {

    @Autowired
    private PatrimonioIntangivelProperties patrimonioIntangivelProperties;

    @Autowired
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Autowired
    private BuscarFornecedoresPorFiltroIntegrationUseCase buscarFornecedoresPorFiltroIntegrationUseCase;

    @Autowired
    private BuscarFornecedorPorIdIntegrationUseCase buscarFornecedorPorIdIntegrationUseCase;

    @Bean("SistemaDeFornecedoresIntegration")
    @Primary
    public SistemaDeFornecedoresIntegration createBean() {
        EfornecedorIntegrationProperties efornecedorProperties = EfornecedorIntegrationProperties
            .builder()
            .url(patrimonioIntangivelProperties.getIntegracao().getEfornecedor().getUrl())
            .build();

        return new SistemaDeFornecedoresIntegrationHalImpl(
            efornecedorProperties,
            sessaoUsuarioDataProvider,
            buscarFornecedoresPorFiltroIntegrationUseCase,
            buscarFornecedorPorIdIntegrationUseCase
        );
    }
}
