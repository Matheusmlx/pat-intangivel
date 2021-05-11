package br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.agendarjobamortizacao;

import br.com.azi.patrimoniointangivel.domain.entity.Calendario;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeConfiguracoesIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.agendarjobamortizacao.converter.AgendarAmortizacaoOutputDataConverter;
import br.com.azi.patrimoniointangivel.utils.date.DateUtils;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
public class AgendarAmortizacaoUseCaseImpl implements AgendarAmortizacaoUseCase {

    private SistemaDeConfiguracoesIntegration sistemaDeConfiguracoesIntegration;

    private AgendarAmortizacaoOutputDataConverter outputDataConverter;

    private String dataMensalAmortizacao;

    private List<String> feriadosNacionais;

    private List<String> feriadosLocais;

    private Clock clock;

    @Override
    public AgendarAmortizacaoOutputData executar() {
        LocalDate diaAgendado = encontrarDiaProximaExecucao();
        String cronExpression = converterDataEmExpressaoCron(diaAgendado);

        salvarExpressaoCronAmortizacao(cronExpression);

        return outputDataConverter.to(diaAgendado, cronExpression);
    }

    private LocalDate encontrarDiaProximaExecucao(){
        LocalDate dAgendamento = LocalDate.of(LocalDate.now(clock).getYear(),LocalDate.now(clock).getMonthValue(),Integer.parseInt(dataMensalAmortizacao));
        int diaUtil = 0;
        int diasCorrridos = 1;
        while (verificaSeDiaAgendadoUtil(dAgendamento)){

            if(diaUtil < Integer.parseInt(dataMensalAmortizacao)){
                diaUtil +=1;
            }
            dAgendamento = DateUtils.somaDias( LocalDate.now(clock), diasCorrridos);
            diasCorrridos += 1;

        }
        return dAgendamento;
    }

    private Boolean verificaSeDiaAgendadoUtil(LocalDate data) {
        feriadosNacionais.addAll(feriadosLocais);
        Calendario calendario = new Calendario(feriadosNacionais);
        return calendario.naoEDiaUtil(data.atStartOfDay());
    }

    private String converterDataEmExpressaoCron(LocalDate data) {
        return String.format("0 5 0 %1$s %2$s ?", data.getDayOfMonth(), data.getMonthValue());
    }

    private void salvarExpressaoCronAmortizacao(String cronExpression) {
        sistemaDeConfiguracoesIntegration.alterarPropriedade("az.patrimonio-intangivel.cron-agendamento-amortizacao", cronExpression);
    }
}
