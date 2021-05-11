package br.com.azi.patrimoniointangivel.configuration.factory.usecase.amortizacao;

import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.calculaquotasconstantes.QuotasConstantesUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.calculaquotasconstantes.QuotasConstantesUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.calculaquotasconstantes.converter.QuotasConstantesOutputDataConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class QuotasConstantesFactory {

    @Bean("QuotasConstantesUseCase")
    @DependsOn({"QuotasConstantesOutputDataConverter"})
    public QuotasConstantesUseCase createUseCase(QuotasConstantesOutputDataConverter outputDataConverter) {
        return new QuotasConstantesUseCaseImpl(outputDataConverter);
    }

    @Bean("QuotasConstantesOutputDataConverter")
    public QuotasConstantesOutputDataConverter createConverter() {
        return new QuotasConstantesOutputDataConverter();
    }
}
