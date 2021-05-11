package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento;

import br.com.azi.patrimoniointangivel.domain.entity.Documento;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.TipoDocumento;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.edicao.EditarDocumentoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.edicao.EditarDocumentoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.edicao.EditarDocumentoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.edicao.converter.EditarDocumentoOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.edicao.converter.EditarPatrimonioOutputDataConverter;
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
public class EditarDocumentoUseCaseTest {

    DocumentoDataProvider documentoDataProvider = Mockito.mock(DocumentoDataProvider.class);
    SistemaDeArquivosIntegration sistemaDeArquivosIntegration = Mockito.mock(SistemaDeArquivosIntegration.class);

    @Test
    public void deveEditarDocumento() {
        EditarDocumentoInputData inputData = EditarDocumentoInputData
            .builder()
            .id(1L)
            .numero("6")
            .idPatrimonio(10L)
            .idTipoDocumento(1L)
            .valor(BigDecimal.valueOf(450))
            .data(Date.from(LocalDateTime.now().withMonth(Month.SEPTEMBER.getValue()).withDayOfMonth(9).atZone(ZoneId.systemDefault()).toInstant()))
            .uriAnexo("documento.pdf")
            .build();

        DocumentoDataProvider documentoDataProvider = Mockito.mock(DocumentoDataProvider.class);
        EditarPatrimonioOutputDataConverter editarPatrimonioOutputDataConverter = new EditarPatrimonioOutputDataConverter();
        EditarDocumentoUseCaseImpl useCase = new EditarDocumentoUseCaseImpl(
            documentoDataProvider,
            new EditarDocumentoOutputDataConverter(),
            sistemaDeArquivosIntegration
        );
        Mockito.when(documentoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Documento
                .builder()
                .id(1L)
                .numero("6")
                .patrimonio(Patrimonio.builder().id(1L).build())
                .tipoDocumento(TipoDocumento.builder().id(2L).build())
                .valor(BigDecimal.valueOf(450))
                .data(LocalDateTime.parse("2019-09-09T10:15:30"))
                .uriAnexo("documento.pdf")
                .build()
            ));
        Mockito.when(documentoDataProvider.atualizar(any(Documento.class)))
            .thenReturn(Documento.builder()
                .id(1L)
                .numero("6")
                .patrimonio(Patrimonio.builder().id(10L).build())
                .tipoDocumento(TipoDocumento.builder().id(1L).build())
                .valor(BigDecimal.valueOf(450))
                .data(LocalDateTime.parse("2019-09-09T10:15:30"))
                .uriAnexo("documento.pdf")
                .build());
        EditarDocumentoOutputData outputData = useCase.executar(inputData);
        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("6", outputData.getNumero());
        Assert.assertEquals("documento.pdf", outputData.getUriAnexo());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalaharQuandoNaopassarId() {
        EditarDocumentoInputData inputData = new EditarDocumentoInputData();
        EditarDocumentoUseCaseImpl usecase = new EditarDocumentoUseCaseImpl(
            documentoDataProvider,
            new EditarDocumentoOutputDataConverter(),
            sistemaDeArquivosIntegration
        );

        usecase.executar(inputData);
    }

}
