package br.com.azi.patrimoniointangivel.configuration.factory.usecase.documentos;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.deletar.DeletarDocumentoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.deletar.DeletarDocumentoUseCaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DeletarDocumentoFactory {

    @Autowired
    private DocumentoDataProvider documentoDataProvider;

    @Bean
    public DeletarDocumentoUseCase deletarDocumentoUseCase() {
        return new DeletarDocumentoUseCaseImpl(documentoDataProvider);
    }
}
