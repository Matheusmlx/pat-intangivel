package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento;

import br.com.azi.patrimoniointangivel.domain.entity.Documento;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.deletar.DeletarDocumentoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.deletar.DeletarDocumentoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.deletar.exception.DocumentoNaoPertenceAoPatrimonioInformadoException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class DeletarDocumentoUseCaseTest {

    @Test
    public void deveDeletarDocumento() {
        DocumentoDataProvider documentoDataProvider = Mockito.mock(DocumentoDataProvider.class);

        DeletarDocumentoInputData inputData = new DeletarDocumentoInputData(1L, 1L);
        DeletarDocumentoUseCaseImpl usecase = new DeletarDocumentoUseCaseImpl(documentoDataProvider);

        Mockito.when(documentoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Documento.builder()
                .id(1L)
                .numero(anyString())
                .patrimonio(Patrimonio.builder().id(1L).build())
                .build()));

        usecase.executar(inputData);

        Mockito.verify(documentoDataProvider, Mockito.times(1)).buscarPorId(any(Long.class));
        Mockito.verify(documentoDataProvider, Mockito.times(1)).remover(any(Long.class));
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdForNulo() {
        DeletarDocumentoInputData inputData = new DeletarDocumentoInputData(null, null);
        DeletarDocumentoUseCaseImpl usecase = new DeletarDocumentoUseCaseImpl(Mockito.mock(DocumentoDataProvider.class));

        usecase.executar(inputData);
    }

    @Test(expected = DocumentoNaoPertenceAoPatrimonioInformadoException.class)
    public void deveFalharQuandoDocumentoNaoForDoPatrimonioInformado() {
        DocumentoDataProvider documentoDataProvider = Mockito.mock(DocumentoDataProvider.class);

        DeletarDocumentoInputData inputData = new DeletarDocumentoInputData(1L, 1L);
        DeletarDocumentoUseCaseImpl usecase = new DeletarDocumentoUseCaseImpl(documentoDataProvider);

        Mockito.when(documentoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Documento.builder()
                .id(1L)
                .numero(anyString())
                .patrimonio(Patrimonio.builder().id(2L).build())
                .build()));

        usecase.executar(inputData);
    }
}
