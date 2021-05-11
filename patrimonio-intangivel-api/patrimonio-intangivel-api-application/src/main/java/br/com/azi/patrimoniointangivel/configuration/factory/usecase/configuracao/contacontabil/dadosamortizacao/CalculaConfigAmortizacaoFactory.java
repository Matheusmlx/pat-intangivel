package br.com.azi.patrimoniointangivel.configuration.factory.usecase.configuracao.contacontabil.dadosamortizacao;

import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.calculadadosamortizacao.CalculaConfigAmortizacaoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.calculadadosamortizacao.CalculaConfigAmortizacaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.calculadadosamortizacao.converter.CalculaConfigAmortizacaoOutputDataConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class CalculaConfigAmortizacaoFactory {

    @Bean("CalculaConfigAmortizacaoUsecase")
    @DependsOn({"CalculaConfigAmortizacaoOutputDataConverter"})
    public CalculaConfigAmortizacaoUseCase createUseCase(CalculaConfigAmortizacaoOutputDataConverter outputDataConverter) {
        return new CalculaConfigAmortizacaoUseCaseImpl(outputDataConverter);
    }

    @Bean("CalculaConfigAmortizacaoOutputDataConverter")
    public CalculaConfigAmortizacaoOutputDataConverter createConverter() {
        return new CalculaConfigAmortizacaoOutputDataConverter();
    }
}
