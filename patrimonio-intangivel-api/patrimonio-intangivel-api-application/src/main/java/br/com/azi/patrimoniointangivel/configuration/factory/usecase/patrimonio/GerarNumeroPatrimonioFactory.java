package br.com.azi.patrimoniointangivel.configuration.factory.usecase.patrimonio;

import br.com.azi.patrimoniointangivel.configuration.properties.patrimoniointangivel.entity.PatrimonioIntangivelProperties;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.patrimonio.GerarNumeroPatrimonioUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.patrimonio.converter.GerarNumeroPatrimonioOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class GerarNumeroPatrimonioFactory {

    @Autowired
    private PatrimonioIntangivelProperties patrimonioIntangivelProperties;

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Bean("GerarNumeroPatrimonioUseCase")
    @DependsOn({"GerarNumeroPatrimonioOutputDataConverter"})
    public GerarNumeroPatrimonioUseCaseImpl createUseCase(GerarNumeroPatrimonioOutputDataConverter outputDataConverter) {
        return new GerarNumeroPatrimonioUseCaseImpl(
            patrimonioDataProvider, patrimonioIntangivelProperties.getQuantidadeDigitosNumeroPatrimonio(), outputDataConverter);
    }

    @Bean("GerarNumeroPatrimonioOutputDataConverter")
    public GerarNumeroPatrimonioOutputDataConverter createConverter() {
        return new GerarNumeroPatrimonioOutputDataConverter();
    }
}
