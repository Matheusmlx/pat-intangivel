package br.com.azi.patrimoniointangivel.configuration.factory.usecase.configuracao.contacontabil.dadosamortizacao;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigAmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.calculadadosamortizacao.CalculaConfigAmortizacaoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.salvardadosamortizacao.SalvaConfigAmortizacaoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.salvardadosamortizacao.SalvaConfigAmortizacaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.salvardadosamortizacao.converter.SalvaConfigAmortizacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class SalvaConfigAmortizacaoFactory {


    @Autowired
    private ConfigAmortizacaoDataProvider amortizacaoDataProvider;

    @Autowired
    private CalculaConfigAmortizacaoUseCase calculaConfigAmortizacaoUseCase;

    @Bean("SalvaConfigAmortizacaoUsecase")
    @DependsOn({"SalvaConfigAmortizacaoOutputDataConverter"})
    public SalvaConfigAmortizacaoUseCase createUseCase(SalvaConfigAmortizacaoOutputDataConverter outputDataConverter) {
        return new SalvaConfigAmortizacaoUseCaseImpl(amortizacaoDataProvider, outputDataConverter, calculaConfigAmortizacaoUseCase);
    }

    @Bean("SalvaConfigAmortizacaoOutputDataConverter")
    public SalvaConfigAmortizacaoOutputDataConverter createConverter() {
        return new SalvaConfigAmortizacaoOutputDataConverter();
    }
}
