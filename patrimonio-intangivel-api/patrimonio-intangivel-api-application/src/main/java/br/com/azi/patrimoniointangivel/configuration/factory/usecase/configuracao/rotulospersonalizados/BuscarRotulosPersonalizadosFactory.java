package br.com.azi.patrimoniointangivel.configuration.factory.usecase.configuracao.rotulospersonalizados;

import br.com.azi.patrimoniointangivel.configuration.properties.patrimoniointangivel.entity.PatrimonioIntangivelProperties;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.rotulospersonalizados.BuscarRotulosPersonalizadosUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.rotulospersonalizados.BuscarRotulosPersonalizadosUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.rotulospersonalizados.converter.RotulosPersonalizadosConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarRotulosPersonalizadosFactory {

    @Autowired
    public PatrimonioIntangivelProperties patrimonioIntangivelProperties;

    @Bean("BuscarRotulosPersonalizadosUseCase")
    @DependsOn("RotulosPersonalizadosConverter")
    public BuscarRotulosPersonalizadosUseCase createUseCase(RotulosPersonalizadosConverter outputDataConverter) {
        return new BuscarRotulosPersonalizadosUseCaseImpl(
            patrimonioIntangivelProperties.getRotulosPersonalizados(),
            outputDataConverter
        );

    }

    @Bean("RotulosPersonalizadosConverter")
    public RotulosPersonalizadosConverter createConverter() {
        return new RotulosPersonalizadosConverter();
    }

}
