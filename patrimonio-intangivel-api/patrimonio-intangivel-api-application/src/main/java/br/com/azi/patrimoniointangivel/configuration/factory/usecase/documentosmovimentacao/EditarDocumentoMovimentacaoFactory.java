package br.com.azi.patrimoniointangivel.configuration.factory.usecase.documentosmovimentacao;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.editar.EditarDocumentoMovimentacaoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.editar.EditarDocumentoMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.editar.converter.EditarDocumentoMovimentacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class EditarDocumentoMovimentacaoFactory {

    @Autowired
    private DocumentoDataProvider documentoDataProvider;

    @Autowired
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Bean("EditarDocumentoMovimentacaoUseCase")
    @DependsOn({"EditarDocumentoMovimentacaoOutputDataConverter"})
    public EditarDocumentoMovimentacaoUseCase createUseCase(EditarDocumentoMovimentacaoOutputDataConverter outputDataConverter){
        return new EditarDocumentoMovimentacaoUseCaseImpl(
            documentoDataProvider,
            sistemaDeArquivosIntegration,
            outputDataConverter
        );
    }

    @Bean("EditarDocumentoMovimentacaoOutputDataConverter")
    public EditarDocumentoMovimentacaoOutputDataConverter createConverter() {
        return new EditarDocumentoMovimentacaoOutputDataConverter();
    }
}
