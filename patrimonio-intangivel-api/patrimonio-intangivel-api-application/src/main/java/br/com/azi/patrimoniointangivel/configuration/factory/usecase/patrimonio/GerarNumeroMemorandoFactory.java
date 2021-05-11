package br.com.azi.patrimoniointangivel.configuration.factory.usecase.patrimonio;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.memorando.GerarNumeroMemorandoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.memorando.converter.GerarNumeroMemorandoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class GerarNumeroMemorandoFactory {

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Bean("GerarNumeroMemorandoUseCase")
    @DependsOn({"GerarNumeroMemorandoOutputDataConverter"})
    public GerarNumeroMemorandoUseCaseImpl createUseCase(GerarNumeroMemorandoOutputDataConverter outputDataConverter) {
        return new GerarNumeroMemorandoUseCaseImpl(patrimonioDataProvider, outputDataConverter);
    }

    @Bean("GerarNumeroMemorandoOutputDataConverter")
    public GerarNumeroMemorandoOutputDataConverter createConverter() {
        return new GerarNumeroMemorandoOutputDataConverter();
    }
}
