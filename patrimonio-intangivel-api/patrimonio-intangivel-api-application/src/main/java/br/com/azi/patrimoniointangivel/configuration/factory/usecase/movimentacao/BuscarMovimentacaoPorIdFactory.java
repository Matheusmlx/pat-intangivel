package br.com.azi.patrimoniointangivel.configuration.factory.usecase.movimentacao;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporid.BuscarMovimentacaoPorIdUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporid.BuscarMovimentacaoPorIdUseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporid.converter.BuscarMovimentacaoPorIdOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarMovimentacaoPorIdFactory {

    @Autowired
    MovimentacaoDataProvider movimentacaoDataProvider;

    @Bean("BuscarMovimentacaoPorIdUseCase")
    @DependsOn({"BuscarMovimentacaoPorIdOutputDataConverter"})
    public BuscarMovimentacaoPorIdUseCase createUseCase(BuscarMovimentacaoPorIdOutputDataConverter outputDataConverter){
      return new BuscarMovimentacaoPorIdUseImpl(movimentacaoDataProvider,outputDataConverter);
    }

    @Bean("BuscarMovimentacaoPorIdOutputDataConverter")
    public BuscarMovimentacaoPorIdOutputDataConverter createConverter(){
        return new BuscarMovimentacaoPorIdOutputDataConverter();
    }

}
