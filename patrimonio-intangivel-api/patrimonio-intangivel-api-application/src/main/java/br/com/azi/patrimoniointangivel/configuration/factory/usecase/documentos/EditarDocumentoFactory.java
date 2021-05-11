package br.com.azi.patrimoniointangivel.configuration.factory.usecase.documentos;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.edicao.EditarDocumentoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.edicao.EditarDocumentoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.edicao.converter.EditarDocumentoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class EditarDocumentoFactory {

    @Autowired
    private DocumentoDataProvider documentoDataProvider;

    @Autowired
    private EditarDocumentoOutputDataConverter editarDocumentoOutputDataConverter;

    @Autowired
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Bean("EditarDocumentoUseCase")
    @DependsOn({"EditarDocumentoOutputDataConverter"})
    public EditarDocumentoUseCase createUseCase(EditarDocumentoOutputDataConverter outputDataConverter) {
        return new EditarDocumentoUseCaseImpl(
            documentoDataProvider,
            editarDocumentoOutputDataConverter,
            sistemaDeArquivosIntegration
        );
    }

    @Bean("EditarDocumentoOutputDataConverter")
    public EditarDocumentoOutputDataConverter createConverter() {
        return new EditarDocumentoOutputDataConverter();
    }
}
