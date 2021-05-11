package br.com.azi.patrimoniointangivel.configuration.factory.usecase.configuracao.contacontabil;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscarporid.BuscarContaContabilPorIdUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscarporid.BuscarContaContabilPorIdUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscarporid.converter.BuscarContaContabilPorIdOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarContaContabilPorIdFactory {

    @Autowired
    private ContaContabilDataProvider contaContabilDataProvider;

    @Autowired
    private ConfigContaContabilDataProvider configContaContabilDataProvider;

    @Bean("BuscarContaContabilPorIdUseCase")
    @DependsOn({"BuscarContaContabilPorIdOutputDataConverter"})
    public BuscarContaContabilPorIdUseCase createUseCase(BuscarContaContabilPorIdOutputDataConverter outputDataConverter) {
        return new BuscarContaContabilPorIdUseCaseImpl(
            contaContabilDataProvider,
            configContaContabilDataProvider,
            outputDataConverter
        );
    }

    @Bean("BuscarContaContabilPorIdOutputDataConverter")
    public BuscarContaContabilPorIdOutputDataConverter createConverter() {
        return new BuscarContaContabilPorIdOutputDataConverter();
    }
}

