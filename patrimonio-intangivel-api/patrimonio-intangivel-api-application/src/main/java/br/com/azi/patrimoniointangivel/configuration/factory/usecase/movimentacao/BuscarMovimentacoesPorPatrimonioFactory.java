package br.com.azi.patrimoniointangivel.configuration.factory.usecase.movimentacao;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporpatrimonio.BuscarMovimentacoesPorPatrimonioUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporpatrimonio.BuscarMovimentacoesPorPatrimonioUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporpatrimonio.converter.BuscarMovimentacoesPorPatrimonioOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarMovimentacoesPorPatrimonioFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private BuscarMovimentacoesPorPatrimonioOutputDataConverter outputDataConverter;

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Bean("BuscarMovimentacoesPorPatrimonioUseCase")
    @DependsOn({"BuscarMovimentacaoPorPatrimonioOutputDataConverter"})
    public BuscarMovimentacoesPorPatrimonioUseCase createUseCase(BuscarMovimentacoesPorPatrimonioOutputDataConverter outputDataConverter){
        return new BuscarMovimentacoesPorPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            movimentacaoDataProvider,
            outputDataConverter);

    }

    @Bean("BuscarMovimentacaoPorPatrimonioOutputDataConverter")
    public BuscarMovimentacoesPorPatrimonioOutputDataConverter createConverter(){
        return new BuscarMovimentacoesPorPatrimonioOutputDataConverter();
    }
}
