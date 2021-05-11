package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento;

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
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.cadastro.expection.NumeroUnicoException;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.cadastro.expection.QuantidadeDeDocumentosCadastradosExcedidoException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    @Mock
    private DocumentoDataProvider documentoDataProvider;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    private TipoDocumentoDataprovider tipoDocumentoDataprovider;

    @Mock
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @InjectMocks
    private CadastrarDocumentoOutputDataConverter outputDataConverter;

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoCadastrarSemNada() {
        CadastrarDocumentoUseCaseImpl useCase = new CadastrarDocumentoUseCaseImpl(patrimonioDataProvider,
            tipoDocumentoDataprovider,
            documentoDataProvider,
            outputDataConverter,
            sistemaDeArquivosIntegration);
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
            outputDataConverter,
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
        Assert.assertEquals("2019-09-09T10:15:30.000-0400", outputData.getData());
        Assert.assertEquals(BigDecimal.valueOf(450), outputData.getValor());
        Assert.assertEquals("documento.pdf", outputData.getUriAnexo());
        Assert.assertEquals(Long.valueOf(1), outputData.getIdTipoDocumento());
        Assert.assertEquals(Long.valueOf(10), outputData.getIdPatrimonio());
    }


    @Test(expected = QuantidadeDeDocumentosCadastradosExcedidoException.class)
    public void deveFalharQuandoHouver30DocumentosJaCadastrados() {
        DocumentoDataProvider documentoDataProvider = Mockito.mock(DocumentoDataProvider.class);
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        CadastrarDocumentoUseCaseImpl useCase = new CadastrarDocumentoUseCaseImpl(
            patrimonioDataProvider,
            tipoDocumentoDataprovider,
            documentoDataProvider,
            outputDataConverter,
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

        Mockito.when(documentoDataProvider.qntDocumentosPorPatrimonioId(any(Long.class)))
            .thenReturn(Long.valueOf(30));

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

        useCase.executar(inputData);
    }

    @Test(expected = NumeroUnicoException.class)
    public void deveFalharQuandoHouverPatrimonioCadastradoComMesmoNumero() {
        DocumentoDataProvider documentoDataProvider = Mockito.mock(DocumentoDataProvider.class);
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        CadastrarDocumentoUseCaseImpl useCase = new CadastrarDocumentoUseCaseImpl(
            patrimonioDataProvider,
            tipoDocumentoDataprovider,
            documentoDataProvider,
            outputDataConverter,
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

        Mockito.when(documentoDataProvider.existeDocumentoComNumero(any(Long.class), any(String.class), any(Long.class)))
            .thenReturn(true);

        useCase.executar(inputData);
    }


}
