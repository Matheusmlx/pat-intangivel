package br.com.azi.patrimoniointangivel.configuration.factory.usecase.documentos;


import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.TipoDocumentoDataprovider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.cadastro.CadastrarDocumentoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.cadastro.CadastrarDocumentoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.cadastro.converter.CadastrarDocumentoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class CadastrarDocumentoFactory {

    @Autowired
    private TipoDocumentoDataprovider tipoDocumentoDataprovider;

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private CadastrarDocumentoOutputDataConverter cadastrarDocumentoOutputDataConverter;

    @Autowired
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Autowired
    private DocumentoDataProvider documentoDataProvider;

    @Bean("CadastrarDocumentoUseCase")
    @DependsOn({"CadastrarDocumentoOutputDataConverter"})
    public CadastrarDocumentoUseCase createUseCase(CadastrarDocumentoOutputDataConverter outputDataConverter) {
        return new CadastrarDocumentoUseCaseImpl(
            patrimonioDataProvider,
            tipoDocumentoDataprovider,
            documentoDataProvider,
            cadastrarDocumentoOutputDataConverter,
            sistemaDeArquivosIntegration
        );
    }

    @Bean("CadastrarDocumentoOutputDataConverter")
    public CadastrarDocumentoOutputDataConverter createConverter() {
        return new CadastrarDocumentoOutputDataConverter();
    }
}
