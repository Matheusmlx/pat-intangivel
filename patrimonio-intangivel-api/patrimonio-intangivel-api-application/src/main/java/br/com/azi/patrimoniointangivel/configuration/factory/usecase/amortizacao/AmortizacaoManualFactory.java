package br.com.azi.patrimoniointangivel.configuration.factory.usecase.amortizacao;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.AmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigAmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DadosAmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.iniciaamortizacaomanual.IniciaAmortizacaoManualUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.iniciaamortizacaomanual.IniciaAmortizacaoManualUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.iniciaamortizacaomanual.converter.IniciaAmortizacaoManualOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.salvadadosamortizacao.SalvaDadosAmortizacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.time.Clock;

@Configuration
public class AmortizacaoManualFactory {

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private ConfigAmortizacaoDataProvider configAmortizacaoDataProvider;

    @Autowired
    private DadosAmortizacaoDataProvider dadosAmortizacaoDataProvider;

    @Autowired
    private SalvaDadosAmortizacaoUseCase salvaDadosAmortizacaoUseCase;

    @Autowired
    private AmortizacaoDataProvider amortizacaoDataProvider;

    @Bean("IniciaAmortizacaoManualUseCase")
    @DependsOn({"IniciaAmortizacaoManualOutputDataConverter", "Clock"})
    public IniciaAmortizacaoManualUseCase createUseCase(IniciaAmortizacaoManualOutputDataConverter outputDataConverter, Clock clock) {
        return new IniciaAmortizacaoManualUseCaseImpl(
            patrimonioDataProvider,
            configAmortizacaoDataProvider,
            dadosAmortizacaoDataProvider,
            salvaDadosAmortizacaoUseCase,
            amortizacaoDataProvider,
            clock,
            outputDataConverter
        );
    }

    @Bean("IniciaAmortizacaoManualOutputDataConverter")
    public IniciaAmortizacaoManualOutputDataConverter createOutputDataConverter() {
        return new IniciaAmortizacaoManualOutputDataConverter();
    }
}
