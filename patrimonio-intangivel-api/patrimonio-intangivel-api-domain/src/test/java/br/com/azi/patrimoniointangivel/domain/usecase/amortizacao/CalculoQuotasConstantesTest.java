package br.com.azi.patrimoniointangivel.domain.usecase.amortizacao;


import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.calculaquotasconstantes.QuotasConstantesInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.calculaquotasconstantes.QuotasConstantesOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.calculaquotasconstantes.QuotasConstantesUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.calculaquotasconstantes.converter.QuotasConstantesOutputDataConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.Month;




@RunWith(MockitoJUnitRunner.class)
public class CalculoQuotasConstantesTest {

    QuotasConstantesOutputData outputData;
    LocalDateTime dataInicio;
    LocalDateTime dataFim;
    QuotasConstantesInputData inputData;


    @Test
    public void deveCalcularAmortizacaoMesCheio() throws ParseException {

        dataInicio = LocalDateTime.now() .withMonth(Month.JANUARY.getValue()) .withDayOfMonth(1)  .withYear(2020);
        dataFim = LocalDateTime.now() .withMonth(Month.JANUARY.getValue()) .withDayOfMonth(31)  .withYear(2020);

        dataInicio = dataInicio.toLocalDate().atStartOfDay();
        dataFim = dataFim.toLocalDate().atTime(23, 59, 59);

        QuotasConstantesUseCaseImpl usecase = new QuotasConstantesUseCaseImpl(new QuotasConstantesOutputDataConverter());
        inputData = new QuotasConstantesInputData(BigDecimal.valueOf(232.32), dataInicio, dataFim, BigDecimal.ZERO,BigDecimal.valueOf(2.78), BigDecimal.valueOf(232.32), false);

       outputData = usecase.executar(inputData);

        Assert.assertEquals(new BigDecimal("225.86").setScale(2, RoundingMode.HALF_EVEN), outputData.getValorPosteiror());
        Assert.assertEquals(BigDecimal.valueOf(232.32), outputData.getValorAnterior());
        Assert.assertEquals(new BigDecimal("6.46").setScale(2, RoundingMode.HALF_EVEN), outputData.getValorSubtraido());

    }

    @Test
    public void deveCalcularAmortizacaoProRata(){

        dataInicio = LocalDateTime.now() .withMonth(Month.JANUARY.getValue()) .withDayOfMonth(8) .withYear(2020);
        dataFim = LocalDateTime.now() .withMonth(Month.JANUARY.getValue()) .withDayOfMonth(31) .withYear(2020);
        QuotasConstantesUseCaseImpl usecase = new QuotasConstantesUseCaseImpl(new QuotasConstantesOutputDataConverter());
        inputData = new QuotasConstantesInputData(BigDecimal.valueOf(232.32), dataInicio, dataFim, BigDecimal.ZERO,BigDecimal.valueOf(2.78), BigDecimal.valueOf(232.32), false);

        outputData = usecase.executar(inputData);

        Assert.assertEquals(new BigDecimal("227.05").setScale(2, RoundingMode.HALF_EVEN), outputData.getValorPosteiror());
        Assert.assertEquals(BigDecimal.valueOf(232.32), outputData.getValorAnterior());
        Assert.assertEquals(new BigDecimal("5.27").setScale(2, RoundingMode.HALF_EVEN), outputData.getValorSubtraido());
        Assert.assertEquals(new BigDecimal("2.27").setScale(2, RoundingMode.HALF_EVEN), outputData.getTaxaAplicada());

    }

    @Test
    public void deveAmortizarUltimoMes(){

        dataInicio = LocalDateTime.now() .withMonth(Month.JANUARY.getValue()) .withDayOfMonth(8) .withYear(2020);
        dataFim = LocalDateTime.now() .withMonth(Month.JANUARY.getValue()) .withDayOfMonth(31) .withYear(2020);
        QuotasConstantesUseCaseImpl usecase = new QuotasConstantesUseCaseImpl(new QuotasConstantesOutputDataConverter());
        QuotasConstantesInputData inputData = new QuotasConstantesInputData();

        inputData.setValorAquisicao(BigDecimal.valueOf(232.32));
        inputData.setDataInicio(dataInicio);
        inputData.setDataFinal(dataFim);
        inputData.setPercentualResidual(BigDecimal.ZERO);
        inputData.setTaxa(BigDecimal.valueOf(2.78));
        inputData.setValorLiquido(BigDecimal.valueOf(232.32));
        inputData.setUltimoMesVidaUtil(true);

        outputData = usecase.executar(inputData);

        Assert.assertEquals(new BigDecimal(0).setScale(2, RoundingMode.HALF_EVEN), outputData.getValorPosteiror());
        Assert.assertEquals(BigDecimal.valueOf(232.32), outputData.getValorAnterior());
        Assert.assertEquals(new BigDecimal("232.32").setScale(2, RoundingMode.HALF_EVEN), outputData.getValorSubtraido());

    }


}
