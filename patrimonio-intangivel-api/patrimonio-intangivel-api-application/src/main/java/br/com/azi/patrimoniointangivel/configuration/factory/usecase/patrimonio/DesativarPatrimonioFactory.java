package br.com.azi.patrimoniointangivel.configuration.factory.usecase.patrimonio;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.AmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigAmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DadosAmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.LancamentosContabeisDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.desativar.DesativarPatrimonioUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.desativar.converter.DesativarPatrimonioOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class DesativarPatrimonioFactory {

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private DadosAmortizacaoDataProvider dadosAmortizacaoDataProvider;

    @Autowired
    private ConfigAmortizacaoDataProvider configAmortizacaoDataProvider;

    @Autowired
    private AmortizacaoDataProvider amortizacaoDataProvider;

    @Autowired
    private LancamentosContabeisDataProvider lancamentosContabeisDataProvider;

    @Bean("DesativarPatrimonioUseCase")
    @DependsOn({"DesativarPatrimonioOutputDataConverter"})
    public DesativarPatrimonioUseCaseImpl createUseCase(DesativarPatrimonioOutputDataConverter outputDataConverter) {
        return new DesativarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            outputDataConverter,
            dadosAmortizacaoDataProvider,
            configAmortizacaoDataProvider,
            amortizacaoDataProvider,
            lancamentosContabeisDataProvider);
    }

    @Bean("DesativarPatrimonioOutputDataConverter")
    public DesativarPatrimonioOutputDataConverter createConverter() {
        return new DesativarPatrimonioOutputDataConverter();
    }
}
