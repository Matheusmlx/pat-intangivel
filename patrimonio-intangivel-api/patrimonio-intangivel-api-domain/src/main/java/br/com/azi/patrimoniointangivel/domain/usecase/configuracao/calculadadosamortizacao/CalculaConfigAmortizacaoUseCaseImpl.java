package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.calculadadosamortizacao;

import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.calculadadosamortizacao.converter.CalculaConfigAmortizacaoOutputDataConverter;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@AllArgsConstructor
public class CalculaConfigAmortizacaoUseCaseImpl implements CalculaConfigAmortizacaoUseCase {

    private CalculaConfigAmortizacaoOutputDataConverter outputDataConverter;

    @Override
    public CalculaConfigAmortizacaoOutputData executar(CalculaConfigAmortizacaoInputData inputData) {
        return outputDataConverter.to(calculaTaxaDepreciacao(inputData));
    }

    private BigDecimal calculaTaxaDepreciacao(CalculaConfigAmortizacaoInputData inputData) {
        return BigDecimal.valueOf(100).divide(
            BigDecimal.valueOf(inputData.getVidaUtil()), new MathContext(10)
        ).setScale(2, RoundingMode.HALF_EVEN);
    }
}
