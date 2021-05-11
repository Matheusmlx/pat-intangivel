package br.com.azi.patrimoniointangivel.domain.usecase.documento.cadastro;

import br.com.azi.patrimoniointangivel.domain.entity.Documento;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.TipoDocumento;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.TipoDocumentoDataprovider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.cadastro.CadastrarDocumentoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.cadastro.CadastrarDocumentoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.cadastro.CadastrarDocumentoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.cadastro.converter.CadastrarDocumentoOutputDataConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class CadastraDocumentoUseCaseTest {

    DocumentoDataProvider documentoDataProvider = Mockito.mock(DocumentoDataProvider.class);
    PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
    TipoDocumentoDataprovider tipoDocumentoDataprovider = Mockito.mock(TipoDocumentoDataprovider.class);
    SistemaDeArquivosIntegration sistemaDeArquivosIntegration = Mockito.mock(SistemaDeArquivosIntegration.class);
    CadastrarDocumentoOutputDataConverter cadastrarDocumentoOutputDataConverter = Mockito.mock(CadastrarDocumentoOutputDataConverter.class);

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoCadastrarSemNada() {
        CadastrarDocumentoUseCaseImpl useCase = new CadastrarDocumentoUseCaseImpl(patrimonioDataProvider, tipoDocumentoDataprovider, documentoDataProvider, new CadastrarDocumentoOutputDataConverter(), sistemaDeArquivosIntegration);
        CadastrarDocumentoInputData inputData = new CadastrarDocumentoInputData();
        useCase.executar(inputData);
    }

    @Test
    public void deveSalvarDocumento() {
        DocumentoDataProvider documentoDataProvider = Mockito.mock(DocumentoDataProvider.class);
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        CadastrarDocumentoUseCaseImpl useCase = new CadastrarDocumentoUseCaseImpl(
            patrimonioDataProvider,
            tipoDocumentoDataprovider,
            documentoDataProvider,
            new CadastrarDocumentoOutputDataConverter(),
            sistemaDeArquivosIntegration
        );
        CadastrarDocumentoInputData inputData = CadastrarDocumentoInputData
            .builder()
            .numero("6")
            .idPatrimonio(10L)
            .idTipoDocumento(1L)
            .valor(BigDecimal.valueOf(450))
            .data(Date.from(LocalDateTime.now().withMonth(Month.SEPTEMBER.getValue()).withDayOfMonth(9).atZone(ZoneId.systemDefault()).toInstant()))
            .build();

        Mockito.when(documentoDataProvider.salvar(any(Documento.class)))
            .thenReturn(Documento
                .builder()
                .id(1L)
                .numero("6")
                .patrimonio(Patrimonio.builder().id(10L).build())
                .tipoDocumento(TipoDocumento.builder().id(1L).build())
                .data(LocalDateTime.parse("2019-09-09T10:15:30"))
                .valor(BigDecimal.valueOf(450))
                .uriAnexo("documento.pdf")
                .build());

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                Patrimonio
                    .builder()
                    .id(7L)
                    .nome("O cara é fera meu")
                    .valorAquisicao(BigDecimal.valueOf(100))
                    .descricao("Brincadeira bicho")
                    .build()
            ));

        Mockito.when(tipoDocumentoDataprovider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                TipoDocumento
                    .builder()
                    .id(1L)
                    .build()
            ));

        CadastrarDocumentoOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("6", outputData.getNumero());
    }
}
