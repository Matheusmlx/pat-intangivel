package br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.calculaquotasconstantes;


import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.calculaquotasconstantes.converter.QuotasConstantesOutputDataConverter;
import br.com.azi.patrimoniointangivel.utils.date.DateUtils;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;


@AllArgsConstructor
public class QuotasConstantesUseCaseImpl implements QuotasConstantesUseCase {

    QuotasConstantesOutputDataConverter outputDataConverter;

    @Override
    public QuotasConstantesOutputData executar(QuotasConstantesInputData inputData) {
        BigDecimal valorAmortizavel = calcularValorAmortizavel(inputData);

        BigDecimal valorAnterior = inputData.getValorLiquido();
        BigDecimal valorSubtraido = calculaValorAmortizacaoPeriodo(inputData, valorAmortizavel);
        BigDecimal valorPosteiror = amortizaPatrimonio(inputData, valorSubtraido);
        BigDecimal taxaAplicada = calculaTaxaAplicada(inputData, valorSubtraido);

        return outputDataConverter.to(valorAnterior, valorSubtraido, valorPosteiror, taxaAplicada);
    }

    private BigDecimal calcularValorAmortizavel(QuotasConstantesInputData inputData) {
        return inputData.getValorAquisicao().subtract(inputData.getValorAquisicao().multiply(inputData.getPercentualResidual(), new MathContext(2, RoundingMode.HALF_EVEN)));
    }

    private BigDecimal calculaValorAmortizacaoPeriodo(QuotasConstantesInputData inputData, BigDecimal valorAmortizavel) {
         if (inputData.getUltimoMesVidaUtil()) {
            return inputData.getValorLiquido();
        } else {
            if (DateUtils.mesCheio(inputData.getDataInicio(), inputData.getDataFinal())) {
                int meses = DateUtils.totalMeses(inputData.getDataInicio(), inputData.getDataFinal());
                BigDecimal valorAmortizacao = valorAmortizavel.multiply(calculaTaxa(inputData.getTaxa() ), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN);
                return BigDecimal.valueOf(meses).multiply(valorAmortizacao, new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN);
            } else {
                BigDecimal amortizacaoDiaria = calculaAmortizacaoDiaria(inputData, valorAmortizavel);
                BigDecimal valorMensal = amortizacaoDiaria.multiply(BigDecimal.valueOf(DateUtils.totalDias(inputData.getDataInicio(), inputData.getDataFinal())), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN);
                return calculaAmortizacaoProRata(inputData, valorMensal).multiply(valorAmortizavel, new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN);
            }
        }
    }

    private BigDecimal calculaAmortizacaoProRata(QuotasConstantesInputData inputData, BigDecimal valorAmortizado) {
        return calculaTaxa(valorAmortizado.multiply(BigDecimal.valueOf(100), new MathContext(10)).divide(inputData.getValorLiquido(), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN));
    }

    private BigDecimal calculaTaxa(BigDecimal taxa) {
        return taxa.divide(BigDecimal.valueOf(100), new MathContext(10)).setScale(6, RoundingMode.HALF_EVEN);
    }

    private BigDecimal calculaAmortizacaoDiaria(QuotasConstantesInputData inputData, BigDecimal valorAmortizavel) {
        BigDecimal valorAmortizacaoMensal = valorAmortizavel.multiply(calculaTaxa(inputData.getTaxa()), new MathContext(10)).setScale(6, RoundingMode.HALF_EVEN);
        return valorAmortizacaoMensal.divide(BigDecimal.valueOf(30), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal amortizaPatrimonio(QuotasConstantesInputData inputData, BigDecimal valorAmortizacao) {
        return inputData.getValorLiquido().subtract(valorAmortizacao, new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal calculaTaxaAplicada(QuotasConstantesInputData inputData, BigDecimal valorAmortizado) {

        if (inputData.getUltimoMesVidaUtil()) {
            return inputData.getValorLiquido().multiply(BigDecimal.valueOf(100), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN).divide( calcularValorAmortizavel(inputData), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN);
        }

        if (DateUtils.mesCheio(inputData.getDataInicio(), inputData.getDataFinal())) {
            return inputData.getTaxa();
        } else {
            return valorAmortizado.multiply(BigDecimal.valueOf(100), new MathContext(10)).divide(inputData.getValorLiquido(), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN);
        }
    }
}
