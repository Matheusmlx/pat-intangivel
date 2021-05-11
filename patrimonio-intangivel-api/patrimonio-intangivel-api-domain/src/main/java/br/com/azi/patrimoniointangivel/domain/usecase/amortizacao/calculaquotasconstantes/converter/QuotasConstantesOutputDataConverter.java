package br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.calculaquotasconstantes.converter;

import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.calculaquotasconstantes.QuotasConstantesOutputData;

import java.math.BigDecimal;

public class QuotasConstantesOutputDataConverter {

    public QuotasConstantesOutputData to(BigDecimal valorAnterior, BigDecimal valorSubtraido,
                                         BigDecimal valorPosterior, BigDecimal taxaAplicada) {
        QuotasConstantesOutputData quotasConstantesOutputData = new QuotasConstantesOutputData();
        quotasConstantesOutputData.setValorAnterior(valorAnterior);
        quotasConstantesOutputData.setValorPosteiror(valorPosterior);
        quotasConstantesOutputData.setValorSubtraido(valorSubtraido);
        quotasConstantesOutputData.setTaxaAplicada(taxaAplicada);
        return quotasConstantesOutputData;
    }

}
