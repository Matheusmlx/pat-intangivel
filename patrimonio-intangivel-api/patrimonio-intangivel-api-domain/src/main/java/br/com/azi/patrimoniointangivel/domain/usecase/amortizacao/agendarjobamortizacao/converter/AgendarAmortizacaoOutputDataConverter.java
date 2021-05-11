package br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.agendarjobamortizacao.converter;

import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.agendarjobamortizacao.AgendarAmortizacaoOutputData;

import java.time.LocalDate;

public class AgendarAmortizacaoOutputDataConverter {

    public AgendarAmortizacaoOutputData to(LocalDate data, String cronExpression) {
        AgendarAmortizacaoOutputData agendarAmortizacaoOutputData = new AgendarAmortizacaoOutputData();
        agendarAmortizacaoOutputData.setData(data);
        agendarAmortizacaoOutputData.setCron(cronExpression);
        return agendarAmortizacaoOutputData;
    }

}
