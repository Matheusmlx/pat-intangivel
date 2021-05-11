package br.com.azi.patrimoniointangivel.configuration.factory.usecase.arquivo;

import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.upload.UploadTemporarioUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.upload.UploadTemporarioUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.upload.converter.UploadTemporarioOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

@Configuration
public class UploadArquivoTemporarioFactory {

    @Autowired
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Bean("UploadTemporarioUseCase")
    @DependsOn({"UploadTemporarioOutputDataConverter"})
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public UploadTemporarioUseCase createUseCase(UploadTemporarioOutputDataConverter converter) {
        return new UploadTemporarioUseCaseImpl(sistemaDeArquivosIntegration, converter);
    }

    @Bean("UploadTemporarioOutputDataConverter")
    public UploadTemporarioOutputDataConverter createOutputDataConverter() {
        return new UploadTemporarioOutputDataConverter();
    }
}
