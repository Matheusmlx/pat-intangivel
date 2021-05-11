package br.com.azi.patrimoniointangivel.configuration.factory.usecase.documentosmovimentacao;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.buscar.BuscarDocumentoMovimentacaoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.buscar.BuscarDocumentoMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.buscar.converter.BuscarDocumentoMovimentacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarDocumentoMovimentacaoFactory {

    @Autowired
    private DocumentoDataProvider documentoDataProvider;

    @Bean("BuscarDocumentoMovimentacaoUseCase")
    @DependsOn({"BuscarDocumentoMovimentacaoOutputDataConverter"})
    public BuscarDocumentoMovimentacaoUseCase createUseCase(BuscarDocumentoMovimentacaoOutputDataConverter outputDataConverter) {
        return new BuscarDocumentoMovimentacaoUseCaseImpl(documentoDataProvider, outputDataConverter);
    }

    @Bean("BuscarDocumentoMovimentacaoOutputDataConverter")
    public BuscarDocumentoMovimentacaoOutputDataConverter createOutputDataConverter() {
        return new BuscarDocumentoMovimentacaoOutputDataConverter();
    }
}
