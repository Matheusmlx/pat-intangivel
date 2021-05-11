package br.com.azi.patrimoniointangivel.entrypoint.amortizacao;

import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.agendarjobamortizacao.AgendarAmortizacaoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.agendarjobamortizacao.AgendarAmortizacaoUseCase;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@EnableScheduling
public class JobAgendarAmortizacaoMensal {

    @Autowired
    private AgendarAmortizacaoUseCase agendarAmortizacaoUseCase;

    @Autowired
    private JobGerarAmortizacaoMensal gerarAmortizacaoMensal;

    @Scheduled(cron = "${az.patrimonio-intangivel.cron-dia-agendamento-amortizacao}")
    public void agendarAmortizacao() {

        Logger.getLogger(GenericConverter.class.getName()).log(Level.INFO, "### Inicio Job Agendador Amortização ###");

        AgendarAmortizacaoOutputData outputData = agendarAmortizacaoUseCase.executar();

        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(1);
        threadPoolTaskScheduler.initialize();

        threadPoolTaskScheduler.schedule(
            gerarAmortizacaoMensal,
            new CronTrigger(outputData.getCron())
        );

        Logger.getLogger(GenericConverter.class.getName()).log(Level.INFO, "### Fim Job Agendador Amortização ###");
    }
}
