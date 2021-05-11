package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.calculadadosamortizacao;

import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.calculadadosamortizacao.converter.CalculaConfigAmortizacaoOutputDataConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@RunWith(MockitoJUnitRunner.class)
public class CalculaConfigAmortizacaoUseCaseImplTest{

    @Mock
    private CalculaConfigAmortizacaoOutputDataConverter outputDataConverter;
    private CalculaConfigAmortizacaoInputData inputData;
    @InjectMocks
    private CalculaConfigAmortizacaoUseCaseImpl calculaConfigAmortizacaoUseCaseImpl;

    @Before
    public void setUp() {
        Whitebox.setInternalState(calculaConfigAmortizacaoUseCaseImpl, "outputDataConverter", outputDataConverter);
    }

    private void setInputData(int vidaUtil) {
        this.inputData = new CalculaConfigAmortizacaoInputData();
        this.inputData.setVidaUtil(vidaUtil);
    }

    @Test
    public void deveChamarOutputDataConverterTo() {
        this.setInputData(50);
        this.calculaConfigAmortizacaoUseCaseImpl.executar(this.inputData);
        Mockito.verify(this.outputDataConverter, Mockito.times(1)).to(Mockito.any(BigDecimal.class));
    }

    @Test
    public void deveExecutarBigDecimalDivideWithThisParams() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        this.setInputData(33);
        Method calculaTaxaDepreciacao = CalculaConfigAmortizacaoUseCaseImpl.class.getDeclaredMethod("calculaTaxaDepreciacao", CalculaConfigAmortizacaoInputData.class);
        calculaTaxaDepreciacao.setAccessible(true);
        BigDecimal taxa = (BigDecimal) calculaTaxaDepreciacao.invoke(this.calculaConfigAmortizacaoUseCaseImpl, this.inputData);

        Assert.assertEquals(taxa, BigDecimal.valueOf(100).divide(
            BigDecimal.valueOf(inputData.getVidaUtil()), new MathContext(10)
        ).setScale(2, RoundingMode.HALF_EVEN));
    }
}
