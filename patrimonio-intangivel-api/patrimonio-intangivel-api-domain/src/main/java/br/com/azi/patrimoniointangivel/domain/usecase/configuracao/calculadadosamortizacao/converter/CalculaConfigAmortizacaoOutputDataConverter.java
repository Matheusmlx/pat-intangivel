package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.calculadadosamortizacao.converter;

import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.calculadadosamortizacao.CalculaConfigAmortizacaoOutputData;

import java.math.BigDecimal;

public class CalculaConfigAmortizacaoOutputDataConverter {

    public CalculaConfigAmortizacaoOutputData to(BigDecimal taxa) {
        CalculaConfigAmortizacaoOutputData calculaConfigAmortizacaoOutputData = new CalculaConfigAmortizacaoOutputData();
        calculaConfigAmortizacaoOutputData.setTaxa(taxa);
        return calculaConfigAmortizacaoOutputData;
    }

    public BigDecimal from(CalculaConfigAmortizacaoOutputData source) {
        throw new UnsupportedOperationException();
    }
}
