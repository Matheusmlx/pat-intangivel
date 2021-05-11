package br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.agendarjobamortizacao;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class AgendarAmortizacaoOutputData {
    private LocalDate data;
    private String cron;
}
