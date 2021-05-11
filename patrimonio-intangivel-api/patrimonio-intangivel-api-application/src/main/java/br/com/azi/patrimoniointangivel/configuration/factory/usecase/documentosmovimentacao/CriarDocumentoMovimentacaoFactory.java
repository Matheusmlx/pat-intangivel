package br.com.azi.patrimoniointangivel.configuration.factory.usecase.documentosmovimentacao;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.TipoDocumentoDataprovider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.cadastro.CriarDocumentoMovimentacaoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.cadastro.CriarDocumentoMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.cadastro.converter.CriarDocumentoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class CriarDocumentoMovimentacaoFactory {

    @Autowired
    private TipoDocumentoDataprovider tipoDocumentoDataprovider;

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private CriarDocumentoOutputDataConverter criarDocumentoOutputDataConverter;

    @Autowired
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Autowired
    private DocumentoDataProvider documentoDataProvider;

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Bean("CriarDocumentoUseCase")
    @DependsOn({"CriarDocumentoOutputDataConverter"})
    public CriarDocumentoMovimentacaoUseCase createUseCase(CriarDocumentoOutputDataConverter outputDataConverter) {
        return new CriarDocumentoMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            tipoDocumentoDataprovider,
            documentoDataProvider,
            sistemaDeArquivosIntegration,
            patrimonioDataProvider,
            outputDataConverter
        );
    }

    @Bean("CriarDocumentoOutputDataConverter")
    public CriarDocumentoOutputDataConverter createConverter() {
        return new CriarDocumentoOutputDataConverter();
    }
}


