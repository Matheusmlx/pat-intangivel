package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento;

import br.com.azi.patrimoniointangivel.domain.entity.Documento;
import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.deletar.DeletarDocumentoMovimentacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.deletar.DeletarDocumentoMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.deletar.exception.DocumentoNaoPertenceAMovimentacaoInformadoException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class DeletarDocumentoMovimentacaoUseCaseTest {

    @Test
    public void deveDeletarDocumentoMovimentacao() {
        DocumentoDataProvider documentoDataProvider = Mockito.mock(DocumentoDataProvider.class);

        DeletarDocumentoMovimentacaoInputData inputData = new DeletarDocumentoMovimentacaoInputData(1L, 1L);
        DeletarDocumentoMovimentacaoUseCaseImpl usecase = new DeletarDocumentoMovimentacaoUseCaseImpl(documentoDataProvider);

        Mockito.when(documentoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Documento.builder()
                .id(1L)
                .numero(anyString())
                .patrimonio(Patrimonio.builder().id(1L).build())
                .movimentacao(Movimentacao.builder().id(1L).build())
                .build()));

        usecase.executar(inputData);

        Mockito.verify(documentoDataProvider, Mockito.times(1)).buscarPorId(any(Long.class));
        Mockito.verify(documentoDataProvider, Mockito.times(1)).remover(any(Long.class));
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdForNulo() {
        DeletarDocumentoMovimentacaoInputData inputData = new DeletarDocumentoMovimentacaoInputData(null, null);
        DeletarDocumentoMovimentacaoUseCaseImpl usecase = new DeletarDocumentoMovimentacaoUseCaseImpl(Mockito.mock(DocumentoDataProvider.class));

        usecase.executar(inputData);
    }

    @Test(expected = DocumentoNaoPertenceAMovimentacaoInformadoException.class)
    public void deveFalharQuandoDocumentoNaoForDaMovimentacaoInformado() {
        DocumentoDataProvider documentoDataProvider = Mockito.mock(DocumentoDataProvider.class);

        DeletarDocumentoMovimentacaoInputData inputData = new DeletarDocumentoMovimentacaoInputData(1L, 1L);
        DeletarDocumentoMovimentacaoUseCaseImpl usecase = new DeletarDocumentoMovimentacaoUseCaseImpl(documentoDataProvider);

        Mockito.when(documentoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Documento.builder()
                .id(1L)
                .numero(anyString())
                .patrimonio(Patrimonio.builder().id(2L).build())
                .movimentacao(Movimentacao.builder().id(2L).build())
                .build()));

        usecase.executar(inputData);
    }
}
