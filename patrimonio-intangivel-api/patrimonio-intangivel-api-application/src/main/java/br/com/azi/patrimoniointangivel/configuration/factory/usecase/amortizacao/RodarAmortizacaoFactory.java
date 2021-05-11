package br.com.azi.patrimoniointangivel.configuration.factory.usecase.amortizacao;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.AmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigAmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DadosAmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.geraamrotizacaomensal.RodarAmortizacaoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.geraamrotizacaomensal.RodarAmortizacaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.salvadadosamortizacao.SalvaDadosAmortizacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.time.Clock;


@Configuration
public class RodarAmortizacaoFactory {

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private SalvaDadosAmortizacaoUseCase salvaDadosAmortizacaoUseCase;

    @Autowired
    private AmortizacaoDataProvider amortizacaoDataProvider;

    @Autowired
    private ConfigAmortizacaoDataProvider configAmortizacaoDataProvider;

    @Autowired
    private DadosAmortizacaoDataProvider dadosAmortizacaoDataProvider;

    @Bean("RodarAmortizacaoUseCase")
    @DependsOn({"Clock"})
    public RodarAmortizacaoUseCase createUseCase(Clock clock) {
        return new RodarAmortizacaoUseCaseImpl(patrimonioDataProvider,
            salvaDadosAmortizacaoUseCase,
            amortizacaoDataProvider,
            configAmortizacaoDataProvider,
            dadosAmortizacaoDataProvider,
            clock);
    }
}
