package br.com.azi.patrimoniointangivel.configuration.factory.usecase.amortizacao;


import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.AmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigAmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DadosAmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.calculaquotasconstantes.QuotasConstantesUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.salvadadosamortizacao.SalvaDadosAmortizacaoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.salvadadosamortizacao.SalvaDadosAmortizacaoUseCaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CalculaAmortizacaoFactory {

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private DadosAmortizacaoDataProvider dadosAmortizacaoDataProvider;

    @Autowired
    private ConfigAmortizacaoDataProvider configAmortizacaoDataProvider;

    @Autowired
    private AmortizacaoDataProvider amortizacaoDataProvider;

    @Autowired
    private QuotasConstantesUseCase quotasConstantesUseCase;

    @Bean("SalvaDadosAmortizacaoUseCase")
    public SalvaDadosAmortizacaoUseCase createUseCase() {
        return new SalvaDadosAmortizacaoUseCaseImpl(
            patrimonioDataProvider,
            dadosAmortizacaoDataProvider,
            configAmortizacaoDataProvider,
            amortizacaoDataProvider,
            quotasConstantesUseCase
        );
    }
}
