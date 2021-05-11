package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.parametro;

import br.com.azi.patrimoniointangivel.domain.entity.PropriedadesProjeto;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.parametro.buscar.BuscarParametrosOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.parametro.buscar.BuscarParametrosUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.parametro.buscar.converter.BuscarParametrosOutputDataConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

@RunWith(MockitoJUnitRunner.class)
public class BuscarParametrosUseCaseTest {

    @Mock
    private PropriedadesProjeto propriedades;
    @Mock
    private BuscarParametrosOutputDataConverter converter;
    @InjectMocks
    private BuscarParametrosUseCaseImpl buscarParametrosUseCase;

    @Before
    public void setUp() {
        Whitebox.setInternalState(buscarParametrosUseCase, "propriedades", propriedades);
        Whitebox.setInternalState(buscarParametrosUseCase, "converter", converter);
    }

    @Test
    public void deveExecutarConverterTo() throws Exception {
        buscarParametrosUseCase.executar();
        PowerMockito.verifyPrivate(this.converter).invoke("to", this.propriedades);
    }

    @Test
    public void deveRetornarParametros() {
        BuscarParametrosOutputData outputData = new BuscarParametrosUseCaseImpl(
            PropriedadesProjeto
                .builder()
                .vidaUtilSemLicenca(Short.valueOf("1"))
                .build(),
            new BuscarParametrosOutputDataConverter()
        ).executar();

        Assert.assertEquals(outputData.getVidaUtilSemLicenca(), Short.valueOf("1"));
    }
}
