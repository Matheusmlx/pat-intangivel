package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscarporid;

import br.com.azi.patrimoniointangivel.domain.entity.ConfigContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscarporid.converter.BuscarContaContabilPorIdOutputDataConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BuscarContaContabilPorIdUseCaseTest {

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoBuscaSemId() {
        ContaContabilDataProvider contaContabilDataProvider = Mockito.mock(ContaContabilDataProvider.class);
        ConfigContaContabilDataProvider configContaContabilDataProvider = Mockito.mock(ConfigContaContabilDataProvider.class);
        BuscarContaContabilPorIdUseCaseImpl useCase = new BuscarContaContabilPorIdUseCaseImpl(
            contaContabilDataProvider,
            configContaContabilDataProvider,
            new BuscarContaContabilPorIdOutputDataConverter()
        );

        BuscarContaContabilPorIdInputData inputData = new BuscarContaContabilPorIdInputData();
        useCase.executar(inputData);
    }

    @Test
    public void deveRetornarContaContabil() {
        ContaContabilDataProvider contaContabilDataProvider = Mockito.mock(ContaContabilDataProvider.class);
        ConfigContaContabilDataProvider configContaContabilDataProvider = Mockito.mock(ConfigContaContabilDataProvider.class);
        BuscarContaContabilPorIdUseCaseImpl useCase = new BuscarContaContabilPorIdUseCaseImpl(
            contaContabilDataProvider,
            configContaContabilDataProvider,
            new BuscarContaContabilPorIdOutputDataConverter()
        );

        Mockito.when(contaContabilDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(ContaContabil
                .builder()
                .id(1L)
                .build()
            ));

        Mockito.when(configContaContabilDataProvider.buscarAtualPorContaContabil(any(Long.class)))
            .thenReturn(Optional.of(
                ConfigContaContabil
                    .builder()
                    .id(1L)
                    .tipo(ConfigContaContabil.Tipo.NAO_AMORTIZAVEL)
                    .build()
            ));

        BuscarContaContabilPorIdInputData inputData = new BuscarContaContabilPorIdInputData(1L, 400L);
        BuscarContaContabilPorIdOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(outputData.getId(), Long.valueOf(1));
    }
}
