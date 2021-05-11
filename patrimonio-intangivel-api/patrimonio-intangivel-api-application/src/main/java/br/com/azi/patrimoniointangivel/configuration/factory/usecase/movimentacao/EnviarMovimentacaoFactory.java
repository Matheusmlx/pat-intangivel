package br.com.azi.patrimoniointangivel.configuration.factory.usecase.movimentacao;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.AmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.enviar.EnviarMovimentacaoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.enviar.EnviarMovimentacaoUseCaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.time.Clock;

@Configuration
public class EnviarMovimentacaoFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private AmortizacaoDataProvider amortizacaoDataProvider;

    @Bean("EnviarMovimentacaoUseCase")
    @DependsOn({"Clock"})
    public EnviarMovimentacaoUseCase createUseCase(Clock clock){
        return new EnviarMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            amortizacaoDataProvider,
            clock);
    }

}
