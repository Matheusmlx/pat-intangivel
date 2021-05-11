package br.com.azi.patrimoniointangivel.configuration.factory.usecase.movimentacao;

import br.com.azi.patrimoniointangivel.configuration.properties.patrimoniointangivel.entity.PatrimonioIntangivelProperties;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.gerarcodigo.GerarCodigoDeMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.gerarcodigo.converter.GerarCodigoDemovimentacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class GerarCodigoDeMovimentacaoFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private PatrimonioIntangivelProperties patrimonioIntangivelProperties;

    @Bean("GerarCodigoDeMovimentacaoUseCase")
    @DependsOn({"GerarCodigoDemovimentacaoOutputDataConverter"})
    public GerarCodigoDeMovimentacaoUseCaseImpl createUseCase(GerarCodigoDemovimentacaoOutputDataConverter outputDataConverter){
        return new GerarCodigoDeMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            patrimonioIntangivelProperties.getQuantidadeDigitosCodigoMovimentacao(),
            outputDataConverter
        );

    }

    @Bean("GerarCodigoDemovimentacaoOutputDataConverter")
    public GerarCodigoDemovimentacaoOutputDataConverter createConverter(){
        return new GerarCodigoDemovimentacaoOutputDataConverter();
    }
}

