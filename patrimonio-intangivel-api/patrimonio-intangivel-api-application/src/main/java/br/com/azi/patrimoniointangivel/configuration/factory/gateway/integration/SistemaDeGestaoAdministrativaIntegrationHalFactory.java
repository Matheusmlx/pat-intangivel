package br.com.azi.patrimoniointangivel.configuration.factory.gateway.integration;

import br.com.azi.patrimoniointangivel.configuration.properties.patrimoniointangivel.entity.PatrimonioIntangivelProperties;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.gateway.integration.hal.SistemaDeGestaoAdministrativaIntegrationHalImpl;
import br.com.azi.patrimoniointangivel.gateway.integration.hal.entity.HalIntegrationProperties;
import br.com.azi.patrimoniointangivel.gateway.integration.hal.usecase.unidadeorganizacional.buscarunidadeorganozacionalporid.BuscarUnidadeOrganizacionalPorIdIntegrationUseCase;
import br.com.azi.patrimoniointangivel.gateway.integration.hal.usecase.unidadeorganizacional.buscarunidadesorganizacionais.BuscarUnidadesOrganizacionaisIntegrationUseCase;
import br.com.azi.patrimoniointangivel.gateway.integration.hal.usecase.unidadeorganizacional.buscarunidadesorganizacionaisporfuncao.BuscarUnidadesOrganizacionaisPorFuncaoIntegrationUseCase;
import br.com.azi.patrimoniointangivel.gateway.integration.hal.usecase.unidadeorganizacional.verificardominiounidadeorganizacionalporidefuncao.VerificarDominioUnidadeOrganizacionalPorIdEFuncaoIntegrationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@RefreshScope
@Configuration
@ConditionalOnProperty(prefix = "az.patrimonio-config.integration", name = "sistema-de-gestao-administrativa", havingValue = "hal", matchIfMissing = true)
public class SistemaDeGestaoAdministrativaIntegrationHalFactory {

    @Autowired
    private PatrimonioIntangivelProperties patrimonioIntangivelProperties;

    @Autowired
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Autowired
    private BuscarUnidadesOrganizacionaisIntegrationUseCase buscarUnidadesOrganizacionaisIntegrationUseCase;

    @Autowired
    private BuscarUnidadesOrganizacionaisPorFuncaoIntegrationUseCase buscarUnidadesOrganizacionaisPorFuncaoIntegrationUseCase;

    @Autowired
    private BuscarUnidadeOrganizacionalPorIdIntegrationUseCase buscarUnidadeOrganizacionalPorIdIntegrationUseCase;

    @Autowired
    private VerificarDominioUnidadeOrganizacionalPorIdEFuncaoIntegrationUseCase verificarDominioUnidadeOrganizacionalPorIdEFuncaoIntegrationUseCase;

    @Bean("SistemaDeGestaoAdministrativaIntegration")
    @Primary
    public SistemaDeGestaoAdministrativaIntegration createBean() {
        HalIntegrationProperties halProperties = HalIntegrationProperties
            .builder()
            .hal(
                HalIntegrationProperties.Hal
                    .builder()
                    .repository(patrimonioIntangivelProperties.getIntegracao().getHal().getRepository())
                    .url(patrimonioIntangivelProperties.getIntegracao().getHal().getUrl())
                    .build()
            )
            .build();

        return new SistemaDeGestaoAdministrativaIntegrationHalImpl(
            halProperties,
            sessaoUsuarioDataProvider,
            buscarUnidadesOrganizacionaisIntegrationUseCase,
            buscarUnidadesOrganizacionaisPorFuncaoIntegrationUseCase,
            buscarUnidadeOrganizacionalPorIdIntegrationUseCase,
            verificarDominioUnidadeOrganizacionalPorIdEFuncaoIntegrationUseCase
        );
    }
}
