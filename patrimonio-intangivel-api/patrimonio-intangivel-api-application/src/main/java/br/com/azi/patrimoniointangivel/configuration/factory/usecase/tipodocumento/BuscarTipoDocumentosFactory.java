package br.com.azi.patrimoniointangivel.configuration.factory.usecase.tipodocumento;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.TipoDocumentoDataprovider;
import br.com.azi.patrimoniointangivel.domain.usecase.tipodocumento.buscardocumentos.BuscarTipoDocumentosUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.tipodocumento.buscardocumentos.BuscarTipoDocumentosUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.tipodocumento.buscardocumentos.converter.BuscarTipoDocumentosOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

@Configuration
public class BuscarTipoDocumentosFactory {

    @Autowired
    private TipoDocumentoDataprovider tipoDocumentoDataprovider;

    @Bean("BuscarTipoDocumentosUseCase")
    @DependsOn({"BuscarTipoDocumentosOutputDataConverter"})
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BuscarTipoDocumentosUseCase createUseCase(BuscarTipoDocumentosOutputDataConverter outputDataConverter) {
        return new BuscarTipoDocumentosUseCaseImpl(tipoDocumentoDataprovider, outputDataConverter);
    }

    @Bean("BuscarTipoDocumentosOutputDataConverter")
    public BuscarTipoDocumentosOutputDataConverter createOutputDataConverter() {
        return new BuscarTipoDocumentosOutputDataConverter();
    }
}


