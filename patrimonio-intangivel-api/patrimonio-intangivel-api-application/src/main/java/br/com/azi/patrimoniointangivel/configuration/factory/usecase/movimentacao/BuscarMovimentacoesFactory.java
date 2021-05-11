package br.com.azi.patrimoniointangivel.configuration.factory.usecase.movimentacao;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarlistagem.BuscarMovimentacoesUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarlistagem.BuscarMovimentacoesUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarlistagem.converter.BuscarMovimentacaoFiltroConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarlistagem.converter.BuscarMovimentacoesOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

@Configuration
public class BuscarMovimentacoesFactory {
    @Autowired
    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Bean("BuscarMovimentacoesUseCase")
    @DependsOn({"BuscarMovimentacoesOutputDataConverter","BuscarMovimentacaoFiltroConverter"})
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BuscarMovimentacoesUseCase createUseCase(BuscarMovimentacoesOutputDataConverter outputDataConverter,BuscarMovimentacaoFiltroConverter filtroConverter){
        return new BuscarMovimentacoesUseCaseImpl(
            filtroConverter,
            sistemaDeGestaoAdministrativaIntegration,
            movimentacaoDataProvider,
            outputDataConverter);
    }

    @Bean("BuscarMovimentacoesOutputDataConverter")
    public BuscarMovimentacoesOutputDataConverter createOutputDataConverter(){
        return new BuscarMovimentacoesOutputDataConverter();
    }

    @Bean("BuscarMovimentacaoFiltroConverter")
    public BuscarMovimentacaoFiltroConverter createFiltroConverter(){
        return new BuscarMovimentacaoFiltroConverter();
    }

}

