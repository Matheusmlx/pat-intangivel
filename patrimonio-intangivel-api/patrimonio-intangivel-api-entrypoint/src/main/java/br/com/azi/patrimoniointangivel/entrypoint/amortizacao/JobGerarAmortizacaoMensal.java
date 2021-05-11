package br.com.azi.patrimoniointangivel.entrypoint.amortizacao;

import br.com.azi.patrimoniointangivel.domain.entity.Job;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.geraamrotizacaomensal.RodarAmortizacaoUseCase;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@EnableScheduling
public class JobGerarAmortizacaoMensal implements Runnable {

    @Autowired
    private RodarAmortizacaoUseCase rodarAmortizacaoUseCase;

    @Override
    @Scheduled(cron = "${az.patrimonio-intangivel.cron-agendamento-amortizacao}")
    public void run() {
        Job job = Job.getInstance();
        Logger.getLogger(GenericConverter.class.getName()).log(Level.INFO, "### Inicio Job Amortizacao ###");
        job.setStatus(Job.Status.EM_ANDAMENTO);
        rodarAmortizacaoUseCase.executar();
        job.setStatus(Job.Status.PARADO);
        Logger.getLogger(GenericConverter.class.getName()).log(Level.INFO, "### Fim Job Amortizacao ###");
    }
}
