package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.remocao;

import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.remocao.exception.PatrimonioNaoPodeSerExcluidoException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class RemoverPatrimonioUseCaseTest {

    @Test(expected = PatrimonioNaoEncontradoException.class)
    public void deveFalharQuandoPatrimonioNaoExistir() {
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        DocumentoDataProvider documentoDataProvider = Mockito.mock(DocumentoDataProvider.class);

        RemoverPatrimonioInputData inputData = new RemoverPatrimonioInputData(1L);
        RemoverPatrimonioUseCaseImpl usecase = new RemoverPatrimonioUseCaseImpl(patrimonioDataProvider, documentoDataProvider);

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        usecase.executar(inputData);
    }

    @Test
    public void deveRemoverPatrimonio() {
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        DocumentoDataProvider documentoDataProvider = Mockito.mock(DocumentoDataProvider.class);

        RemoverPatrimonioInputData inputData = new RemoverPatrimonioInputData(1L);
        RemoverPatrimonioUseCaseImpl usecase = new RemoverPatrimonioUseCaseImpl(patrimonioDataProvider, documentoDataProvider);

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("Software intagivel")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .build()));

        usecase.executar(inputData);

        Mockito.verify(patrimonioDataProvider, Mockito.times(1)).buscarPorId(any(Long.class));
        Mockito.verify(patrimonioDataProvider, Mockito.times(1)).remover(any(Long.class));
    }

    @Test(expected = PatrimonioNaoPodeSerExcluidoException.class)
    public void deveFalharQuandoPatrimonioEstiverAtivo() {
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        DocumentoDataProvider documentoDataProvider = Mockito.mock(DocumentoDataProvider.class);
        ;

        RemoverPatrimonioInputData inputData = new RemoverPatrimonioInputData(1L);
        RemoverPatrimonioUseCaseImpl usecase = new RemoverPatrimonioUseCaseImpl(patrimonioDataProvider, documentoDataProvider);

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("Software intagivel")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.ATIVO)
                .build()));

        usecase.executar(inputData);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoBuscaSemId() {
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        DocumentoDataProvider documentoDataProvider = Mockito.mock(DocumentoDataProvider.class);
        ;

        RemoverPatrimonioInputData inputData = new RemoverPatrimonioInputData(null);
        RemoverPatrimonioUseCaseImpl usecase = new RemoverPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            documentoDataProvider);

        usecase.executar(inputData);
    }
}
