package br.com.azi.patrimoniointangivel.configuration.factory.usecase.arquivo;

import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.download.DownloadUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.download.DownloadUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.download.converter.DownloadOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

@Configuration
public class DownloadArquivoFactory {

    @Autowired
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Bean("DownloadUseCase")
    @DependsOn({"DownloadOutputDataConverter"})
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public DownloadUseCase createUseCase(DownloadOutputDataConverter converter) {
        return new DownloadUseCaseImpl(sistemaDeArquivosIntegration, converter);
    }

    @Bean("DownloadOutputDataConverter")
    public DownloadOutputDataConverter createOutputDataConverter() {
        return new DownloadOutputDataConverter();
    }
}
