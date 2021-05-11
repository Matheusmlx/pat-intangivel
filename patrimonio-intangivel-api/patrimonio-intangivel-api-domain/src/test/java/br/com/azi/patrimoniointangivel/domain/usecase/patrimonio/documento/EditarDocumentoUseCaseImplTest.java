package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento;

import br.com.azi.patrimoniointangivel.domain.entity.Documento;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.TipoDocumento;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.cadastro.expection.NumeroUnicoException;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.edicao.EditarDocumentoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.edicao.EditarDocumentoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.edicao.EditarDocumentoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.edicao.converter.EditarDocumentoOutputDataConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class EditarDocumentoUseCaseImplTest {

    @Mock
    private DocumentoDataProvider documentoDataProvider;

    @Mock
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @InjectMocks
    private EditarDocumentoOutputDataConverter outputDataConverter;

    @Test(expected = IllegalStateException.class)
    public void deveFalharSeNaoPassarIdDocumento() {
        EditarDocumentoUseCaseImpl useCase = new EditarDocumentoUseCaseImpl(
            documentoDataProvider,
            outputDataConverter,
            sistemaDeArquivosIntegration);

        useCase.executar(new EditarDocumentoInputData());
    }

    @Test(expected = NumeroUnicoException.class)
    public void deveFalharSeQuandoNumeroDocumentoJaExistir() {
        EditarDocumentoUseCaseImpl useCase = new EditarDocumentoUseCaseImpl(
            documentoDataProvider,
            outputDataConverter,
            sistemaDeArquivosIntegration);

        Mockito.when(documentoDataProvider.existeDocumentoComNumero(
            any(Long.class), any(String.class), any(Long.class))).thenReturn(Boolean.TRUE);

        EditarDocumentoInputData inputData = EditarDocumentoInputData.builder()
            .id(1L)
            .idPatrimonio(2L)
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
        EditarDocumentoUseCaseImpl useCase = new EditarDocumentoUseCaseImpl(
            documentoDataProvider,
            outputDataConverter,
            sistemaDeArquivosIntegration);

        Mockito.when(documentoDataProvider.existeDocumentoComNumero(
            any(Long.class), any(String.class), any(Long.class))).thenReturn(Boolean.FALSE);

        Mockito.when(documentoDataProvider.buscarPorId( any(Long.class))).thenReturn(
            Optional.of(Documento.builder()
                .id(1L)
                .uriAnexo("uri")
                .build()));

        Mockito.when(documentoDataProvider.atualizar(any(Documento.class)))
            .thenReturn(Documento.builder()
                .id(1L)
                .uriAnexo("uri")
                .numero("001")
                .valor(BigDecimal.TEN)
                .tipoDocumento(TipoDocumento.builder().id(2L).build())
                .patrimonio(Patrimonio.builder().id(3L).build())
                .build());

        EditarDocumentoInputData inputData = EditarDocumentoInputData.builder()
            .id(1L)
            .idPatrimonio(2L)
            .idTipoDocumento(3L)
            .data(new Date())
            .numero("001")
            .valor(BigDecimal.TEN)
            .build();

        EditarDocumentoOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1L), outputData.getId());
        Assert.assertEquals("001", outputData.getNumero());
        Assert.assertEquals(BigDecimal.valueOf(10), outputData.getValor());
        Assert.assertEquals(Long.valueOf(2L), outputData.getIdTipoDocumento());
    }
}
