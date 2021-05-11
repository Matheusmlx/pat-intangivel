package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.editar;

import br.com.azi.patrimoniointangivel.domain.entity.ConfigContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.editar.converter.EditarContaContabilOutputDataConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static br.com.azi.patrimoniointangivel.domain.entity.ConfigContaContabil.Metodo.QUOTAS_CONSTANTES;
import static br.com.azi.patrimoniointangivel.domain.entity.ConfigContaContabil.Tipo.AMORTIZAVEL;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class EditarContaContabilUseCaseImplTest {

    @Test
    public void deveEditarContaContabil(){
        ConfigContaContabilDataProvider configContaContabilDataProvider = Mockito.mock(ConfigContaContabilDataProvider.class);

        EditarContaContabilInputData inputData = EditarContaContabilInputData
            .builder()
            .id(1L)
            .contaContabil(2L)
            .tipo("AMORTIZAVEL")
            .build();
        EditarContaContabilOutputDataConverter editarContaContabilOutputDataConverter = new EditarContaContabilOutputDataConverter();

        EditarContaContabilUseCaseImpl useCase = new EditarContaContabilUseCaseImpl(
            configContaContabilDataProvider,editarContaContabilOutputDataConverter);

        Mockito.when(configContaContabilDataProvider.atualizar(any(ConfigContaContabil.class)))
            .thenReturn(ConfigContaContabil.builder()
                .id(1L)
                .contaContabil(ContaContabil.builder().id(2L).build())
                .metodo(QUOTAS_CONSTANTES)
                .tipo(AMORTIZAVEL)
                .build());

        EditarContaContabilOutputData outputData = useCase.executar(inputData);
        Assert.assertEquals(Long.valueOf(1),outputData.getId());
        Assert.assertEquals("AMORTIZAVEL",outputData.getTipo());
        Assert.assertEquals("QUOTAS_CONSTANTES",outputData.getMetodo());
    }

    @Test(expected = IllegalStateException.class)
    public void deveLancarException() {
        ConfigContaContabilDataProvider configContaContabilDataProvider = Mockito.mock(ConfigContaContabilDataProvider.class);
        EditarContaContabilUseCaseImpl editarContaContabilUseCase = new EditarContaContabilUseCaseImpl(configContaContabilDataProvider, new EditarContaContabilOutputDataConverter());

        EditarContaContabilInputData inputData = new EditarContaContabilInputData(Long.MAX_VALUE, Long.MAX_VALUE, null);
        editarContaContabilUseCase.executar(inputData);
    }
}
