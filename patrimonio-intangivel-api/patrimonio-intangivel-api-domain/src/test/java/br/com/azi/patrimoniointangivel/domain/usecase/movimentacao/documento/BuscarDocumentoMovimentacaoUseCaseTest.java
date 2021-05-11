package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento;

import br.com.azi.patrimoniointangivel.domain.entity.Documento;
import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.TipoDocumento;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.buscar.BuscarDocumentoMovimentacaoIntputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.buscar.BuscarDocumentoMovimentacaoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.buscar.BuscarDocumentoMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.buscar.converter.BuscarDocumentoMovimentacaoOutputDataConverter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuscarDocumentoMovimentacaoUseCaseTest {

    @InjectMocks
    BuscarDocumentoMovimentacaoUseCaseImpl useCase;

    @InjectMocks
    BuscarDocumentoMovimentacaoOutputDataConverter outputDataConverter;

    @Mock
    DocumentoDataProvider documentoDataProvider;

    @Before
    public void criarInstancia(){
        useCase = new BuscarDocumentoMovimentacaoUseCaseImpl(documentoDataProvider,outputDataConverter);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoPassarIdMovimentacao(){
        useCase.executar(BuscarDocumentoMovimentacaoIntputData.builder().build());
    }

    @Test
    public void deveBuscarDocumento(){
        BuscarDocumentoMovimentacaoIntputData intputData = new BuscarDocumentoMovimentacaoIntputData(1L);

        when(documentoDataProvider.buscarDocumentoPorMovimentacaoId(intputData.getMovimentacaoId())).thenReturn(
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
                    .movimentacao(Movimentacao.builder().id(2L).build())
                    .build()));

        BuscarDocumentoMovimentacaoOutputData outputData = useCase.executar(intputData);

        assertEquals(1,outputData.getItems().size());
        assertEquals(Long.valueOf(1), outputData.getItems().get(0).getId());
        assertEquals("1234556", outputData.getItems().get(0).getNumero());
        assertEquals("2020-09-07T13:53:20.000-0400", outputData.getItems().get(0).getData());
        assertEquals(BigDecimal.TEN, outputData.getItems().get(0).getValor());
        assertEquals(Long.valueOf(1), outputData.getItems().get(0).getIdTipoDocumento());
        assertEquals(Long.valueOf(1), outputData.getItems().get(0).getIdPatrimonio());
        assertEquals(Long.valueOf(2), outputData.getItems().get(0).getIdMovimentacao());
    }
}
