package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento;

import br.com.azi.patrimoniointangivel.domain.entity.Documento;
import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.TipoDocumento;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.editar.EditarDocumentoMovimentacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.editar.EditarDocumentoMovimentacaoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.editar.EditarDocumentoMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.editar.converter.EditarDocumentoMovimentacaoOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.editar.exception.NumeroUnicoException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EditarDocumentoMovimentacaoUseCaseTest {

    @Mock
    private DocumentoDataProvider documentoDataProvider;

    @Mock
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @InjectMocks
    private EditarDocumentoMovimentacaoOutputDataConverter outputDataConverter;

    @InjectMocks
    private EditarDocumentoMovimentacaoUseCaseImpl useCase;

    @Before
    public void gerarInstanciaDoUseCase(){
        useCase = new EditarDocumentoMovimentacaoUseCaseImpl(documentoDataProvider,sistemaDeArquivosIntegration,outputDataConverter);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharSeNaoPassarIdDocumento() {
        useCase.executar(new EditarDocumentoMovimentacaoInputData());
    }

    @Test(expected = NumeroUnicoException.class)
    public void deveFalharSeQuandoNumeroDocumentoJaExistir() {
        when(documentoDataProvider.buscarPorId(any(Long.class))).thenReturn(Optional.of(Documento.builder().id(2L).build()));

        when(documentoDataProvider.existeDocumentoMovimentacaoComNumero( any(Long.class), any(String.class), any(Long.class))).thenReturn(Boolean.TRUE);

        EditarDocumentoMovimentacaoInputData inputData = EditarDocumentoMovimentacaoInputData.builder()
            .id(1L)
            .idMovimentacao(2L)
            .idTipoDocumento(3L)
            .data(new Date())
            .numero("001")
            .uriAnexo("")
            .valor(BigDecimal.TEN)
            .build();

        useCase.executar(inputData);
    }

    @Test
    public void deveEditarDocumento() {

        when(documentoDataProvider.buscarPorId(any(Long.class))).thenReturn(
            Optional.of(Documento.builder()
                .id(1L)
                .uriAnexo("uri")
                .build()));

        when(documentoDataProvider.atualizar(any(Documento.class)))
            .thenReturn(Documento.builder()
                .id(1L)
                .uriAnexo("uri")
                .numero("001")
                .valor(BigDecimal.TEN)
                .tipoDocumento(TipoDocumento.builder().id(2L).build())
                .patrimonio(Patrimonio.builder().id(3L).build())
                .movimentacao(Movimentacao.builder().id(2L).build())
                .build());

        EditarDocumentoMovimentacaoInputData inputData = EditarDocumentoMovimentacaoInputData.builder()
            .id(1L)
            .idMovimentacao(2L)
            .idTipoDocumento(3L)
            .data(new Date())
            .numero("001")
            .valor(BigDecimal.TEN)
            .build();

        EditarDocumentoMovimentacaoOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1L), outputData.getId());
        Assert.assertEquals(Long.valueOf(2L), outputData.getIdMovimentacao());
        Assert.assertEquals("001", outputData.getNumero());
        Assert.assertEquals(BigDecimal.valueOf(10), outputData.getValor());
        Assert.assertEquals(Long.valueOf(2L), outputData.getIdTipoDocumento());
    }
}
