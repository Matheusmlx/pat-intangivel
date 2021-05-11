package br.com.azi.patrimoniointangivel.configuration.factory.usecase.amortizacao;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.AmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.buscarlistagem.BuscarAmortizacaoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.buscarlistagem.BuscarAmortizacaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.buscarlistagem.converter.BuscarAmortizacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

@Configuration
public class BuscaAmortizacaoFactory {

    @Autowired
    private AmortizacaoDataProvider amortizacaoDataProvider;

    @Bean("BuscarAmortizacaoUseCase")
    @DependsOn({"BuscarAmortizacaoOutputDataConverter"})
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BuscarAmortizacaoUseCase createUseCase(BuscarAmortizacaoOutputDataConverter outputDataConverter) {
        return new BuscarAmortizacaoUseCaseImpl(amortizacaoDataProvider, outputDataConverter);
    }

    @Bean("BuscarAmortizacaoOutputDataConverter")
    public BuscarAmortizacaoOutputDataConverter createOutputDataConverter() {
        return new BuscarAmortizacaoOutputDataConverter();
    }
}
