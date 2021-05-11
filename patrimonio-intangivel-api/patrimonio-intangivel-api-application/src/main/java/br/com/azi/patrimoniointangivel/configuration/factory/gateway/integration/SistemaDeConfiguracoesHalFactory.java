package br.com.azi.patrimoniointangivel.configuration.factory.gateway.integration;

import br.com.azi.patrimoniointangivel.configuration.properties.patrimoniointangivel.entity.PatrimonioIntangivelProperties;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeConfiguracoesIntegration;
import br.com.azi.patrimoniointangivel.gateway.integration.hal.SistemaDeConfiguracoesIntegrationHalImpl;
import br.com.azi.patrimoniointangivel.gateway.integration.hal.entity.HalIntegrationProperties;
import br.com.azi.patrimoniointangivel.gateway.integration.hal.usecase.alterarpropriedade.AlterarPropriedadeIntegrationUseCase;
import br.com.azi.patrimoniointangivel.gateway.integration.hal.usecase.autenticar.AutenticarIntegrationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@RefreshScope
@Configuration
public class SistemaDeConfiguracoesHalFactory {

    @Autowired
    private PatrimonioIntangivelProperties patrimonioIntangivelProperties;

    @Autowired
    private AlterarPropriedadeIntegrationUseCase alterarPropriedadeIntegrationUseCase;

    @Autowired
    private AutenticarIntegrationUseCase autenticarIntegrationUseCase;

    @Bean("SistemaDeConfiguracoesIntegration")
    @Primary
    public SistemaDeConfiguracoesIntegration createBean() {
        HalIntegrationProperties halProperties = HalIntegrationProperties
            .builder()
            .hal(
                HalIntegrationProperties.Hal
                    .builder()
                    .repository(patrimonioIntangivelProperties.getIntegracao().getHal().getRepository())
                    .url(patrimonioIntangivelProperties.getIntegracao().getHal().getUrl())
                    .build()
            )
            .halConfig(
                HalIntegrationProperties.HalConfig
                    .builder()
                    .url(patrimonioIntangivelProperties.getIntegracao().getHalConfig().getUrl())
                    .build()
            )
            .usuario(
                HalIntegrationProperties.Usuario
                    .builder()
                    .login(patrimonioIntangivelProperties.getIntegracao().getUsuario().getLogin())
                    .senha(patrimonioIntangivelProperties.getIntegracao().getUsuario().getSenha())
                    .build()
            )
            .build();

        return new SistemaDeConfiguracoesIntegrationHalImpl(
            halProperties,
            autenticarIntegrationUseCase,
            alterarPropriedadeIntegrationUseCase
        );
    }
}
