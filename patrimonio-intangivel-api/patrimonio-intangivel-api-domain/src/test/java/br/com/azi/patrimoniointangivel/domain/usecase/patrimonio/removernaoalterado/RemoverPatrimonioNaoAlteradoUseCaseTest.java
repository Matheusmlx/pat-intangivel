package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.removernaoalterado;

import br.com.azi.patrimoniointangivel.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.remocao.RemoverPatrimonioInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.remocao.RemoverPatrimonioUseCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RemoverPatrimonioNaoAlteradoUseCaseTest {
    @InjectMocks
    private RemoverPatrimonioNaoAlteradoUseCaseImpl useCase;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    private RemoverPatrimonioUseCase removerPatrimonioUseCase;

    @Before
    public void setup(){
        useCase =  new RemoverPatrimonioNaoAlteradoUseCaseImpl(patrimonioDataProvider,removerPatrimonioUseCase);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoBuscaSemId() {
        useCase.executar(new RemoverPatrimonioNaoAlteradoInputData(null));
    }

    @Test(expected = PatrimonioNaoEncontradoException.class)
    public void deveFalharQuandoPatrimonioNaoExistir() {
        when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        useCase.executar(new RemoverPatrimonioNaoAlteradoInputData(1L));
    }

    @Test
    public void deveRemoverPatrimonio() {
        when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .build()));

        useCase.executar(new RemoverPatrimonioNaoAlteradoInputData(1L));

        Mockito.verify(patrimonioDataProvider, Mockito.times(1)).buscarPorId(any(Long.class));
        Mockito.verify(removerPatrimonioUseCase, Mockito.times(1)).executar(any(RemoverPatrimonioInputData.class));
    }

    @Test
    public void naoDeveRemoverPatrimonioPoisEleForAlterado() {
        when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("teste")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .build()));

        useCase.executar(new RemoverPatrimonioNaoAlteradoInputData(1L));

        Mockito.verify(patrimonioDataProvider, Mockito.times(1)).buscarPorId(any(Long.class));
        Mockito.verify(removerPatrimonioUseCase, Mockito.never()).executar(any(RemoverPatrimonioInputData.class));
    }
}
