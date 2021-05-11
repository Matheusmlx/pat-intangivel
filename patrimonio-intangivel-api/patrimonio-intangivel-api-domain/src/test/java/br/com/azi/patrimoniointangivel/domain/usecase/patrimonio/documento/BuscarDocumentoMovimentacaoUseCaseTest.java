package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento;

import br.com.azi.patrimoniointangivel.domain.entity.Documento;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.TipoDocumento;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.buscar.BuscarDocumentoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.buscar.BuscarDocumentoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.buscar.BuscarDocumentoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.buscar.converter.BuscarDocumentoOutputDataConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class BuscarDocumentoMovimentacaoUseCaseTest {

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoPassarIdPatrimonio(){
        BuscarDocumentoInputData inputData = new BuscarDocumentoInputData(null);
        BuscarDocumentoUseCaseImpl usecase = new BuscarDocumentoUseCaseImpl(Mockito.mock(DocumentoDataProvider.class),
            Mockito.mock(BuscarDocumentoOutputDataConverter.class));

        usecase.executar(inputData);
    }

    @Test
    public void deveBuscarDocumento(){
        DocumentoDataProvider documentoDataProvider = Mockito.mock(DocumentoDataProvider.class);
        BuscarDocumentoInputData inputData = new BuscarDocumentoInputData(1L);

        Mockito.when(documentoDataProvider.buscarDocumentoPorPatrimonioId(inputData.getPatrimonioId())).thenReturn(
            Collections.singletonList(
                 Documento
                .builder()
                .id(1L)
                .numero("1234556")
                .data(LocalDateTime
                    .ofEpochSecond(50000, 50000, ZoneOffset.UTC)
                    .withYear(2020)
                    .withDayOfMonth(7)
                    .withMonth(Month.SEPTEMBER.getValue()) )
                .valor(BigDecimal.TEN)
                .uriAnexo("setup.anexo")
                .tipoDocumento(TipoDocumento.builder()
                    .id(1L)
                    .build())
                .patrimonio(Patrimonio.builder() .id(1L) .build())
                .build()));

        BuscarDocumentoUseCaseImpl usecase = new BuscarDocumentoUseCaseImpl(documentoDataProvider,
            new BuscarDocumentoOutputDataConverter());

        BuscarDocumentoOutputData outputData = usecase.executar(inputData);

        Assert.assertEquals(1, outputData.getItems().size());
        Assert.assertEquals(Long.valueOf(1), outputData.getItems().get(0).getId());
        Assert.assertEquals("1234556", outputData.getItems().get(0).getNumero());
        Assert.assertEquals("2020-09-07T13:53:20.000-0400", outputData.getItems().get(0).getData());
        Assert.assertEquals(BigDecimal.TEN, outputData.getItems().get(0).getValor());
        Assert.assertEquals(Long.valueOf(1), outputData.getItems().get(0).getIdTipoDocumento());
        Assert.assertEquals(Long.valueOf(1), outputData.getItems().get(0).getIdPatrimonio());
    }
}
