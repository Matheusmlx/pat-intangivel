package br.com.azi.patrimoniointangivel.configuration.factory.usecase.documentos;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.buscar.BuscarDocumentoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.buscar.BuscarDocumentoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.buscar.converter.BuscarDocumentoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;


@Configuration
public class BuscarDocumentoFactory {

    @Autowired
    private DocumentoDataProvider documentoDataProvider;

    @Bean("BuscarDocumentoUseCase")
    @DependsOn({"BuscarDocumentoOutputDataConverter"})
    public BuscarDocumentoUseCase createUseCase(BuscarDocumentoOutputDataConverter outputDataConverter) {
        return new BuscarDocumentoUseCaseImpl(documentoDataProvider, outputDataConverter);
    }

    @Bean("BuscarDocumentoOutputDataConverter")
    public BuscarDocumentoOutputDataConverter createOutputDataConverter() {
        return new BuscarDocumentoOutputDataConverter();
    }
}
