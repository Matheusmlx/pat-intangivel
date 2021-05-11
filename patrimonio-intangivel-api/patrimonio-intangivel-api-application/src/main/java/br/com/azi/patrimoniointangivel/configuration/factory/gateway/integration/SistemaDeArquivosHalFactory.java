package br.com.azi.patrimoniointangivel.configuration.factory.gateway.integration;

import br.com.azi.patrimoniointangivel.configuration.properties.patrimoniointangivel.entity.PatrimonioIntangivelProperties;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniointangivel.gateway.integration.hal.SistemaDeArquivosIntegrationHalImpl;
import br.com.azi.patrimoniointangivel.gateway.integration.hal.entity.HalIntegrationProperties;
import br.com.azi.patrimoniointangivel.gateway.integration.hal.usecase.downloadfile.DownloadFileIntegrationUseCase;
import br.com.azi.patrimoniointangivel.gateway.integration.hal.usecase.metadatafile.MetadataFileIntegrationUseCase;
import br.com.azi.patrimoniointangivel.gateway.integration.hal.usecase.promotefile.PromoteFileIntegrationUseCase;
import br.com.azi.patrimoniointangivel.gateway.integration.hal.usecase.removedefinitivefile.RemoveDefinitiveFileIntegrationUseCase;
import br.com.azi.patrimoniointangivel.gateway.integration.hal.usecase.uploadfile.UploadFileIntegrationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@RefreshScope
@Configuration
@ConditionalOnProperty(prefix = "az.patrimonio-intangivel.integration", name = "sistema-de-arquivos", havingValue = "hal", matchIfMissing = true)
public class SistemaDeArquivosHalFactory {

    @Autowired
    private PatrimonioIntangivelProperties patrimonioIntangivelProperties;

    @Autowired
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Autowired
    private UploadFileIntegrationUseCase uploadFileIntegrationUseCase;

    @Autowired
    private PromoteFileIntegrationUseCase promoteFileIntegrationUseCase;

    @Autowired
    private MetadataFileIntegrationUseCase metadataFileIntegrationUseCase;

    @Autowired
    private DownloadFileIntegrationUseCase downloadFileIntegrationUseCase;

    @Autowired
    private RemoveDefinitiveFileIntegrationUseCase removeDefinitiveFileIntegrationUseCase;

    @Bean("SistemaDeArquivosIntegration")
    @Primary
    public SistemaDeArquivosIntegration createBean() {
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

        return new SistemaDeArquivosIntegrationHalImpl(
            halProperties,
            sessaoUsuarioDataProvider,
            uploadFileIntegrationUseCase,
            promoteFileIntegrationUseCase,
            metadataFileIntegrationUseCase,
            downloadFileIntegrationUseCase,
            removeDefinitiveFileIntegrationUseCase
        );
    }
}
