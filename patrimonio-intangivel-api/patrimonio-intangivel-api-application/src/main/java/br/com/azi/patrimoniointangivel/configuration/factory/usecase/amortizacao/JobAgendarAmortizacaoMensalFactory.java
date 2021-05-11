package br.com.azi.patrimoniointangivel.configuration.factory.usecase.amortizacao;

import br.com.azi.patrimoniointangivel.configuration.properties.patrimoniointangivel.entity.PatrimonioIntangivelProperties;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeConfiguracoesIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.agendarjobamortizacao.AgendarAmortizacaoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.agendarjobamortizacao.AgendarAmortizacaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.agendarjobamortizacao.converter.AgendarAmortizacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.time.Clock;

@Configuration
public class JobAgendarAmortizacaoMensalFactory {

    @Autowired
    private PatrimonioIntangivelProperties patrimonioIntangivelProperties;

    @Autowired
    private SistemaDeConfiguracoesIntegration sistemaDeConfiguracoesIntegration;


    @Bean("AgendarAmortizacaoUseCase")
    @DependsOn({"AgendarAmortizacaoOutputDataConverter", "Clock"})
    public AgendarAmortizacaoUseCase createUseCase(AgendarAmortizacaoOutputDataConverter outputDataConverter, Clock clock) {
        return new AgendarAmortizacaoUseCaseImpl(
            sistemaDeConfiguracoesIntegration,
            outputDataConverter,
            patrimonioIntangivelProperties.getDataMensalAmortizacao(),
            patrimonioIntangivelProperties.getFeriadosNacionais(),
            patrimonioIntangivelProperties.getFeriadosLocais(),
            clock
        );
    }

    @Bean("AgendarAmortizacaoOutputDataConverter")
    public AgendarAmortizacaoOutputDataConverter createConverter() {
        return new AgendarAmortizacaoOutputDataConverter();
    }
}
