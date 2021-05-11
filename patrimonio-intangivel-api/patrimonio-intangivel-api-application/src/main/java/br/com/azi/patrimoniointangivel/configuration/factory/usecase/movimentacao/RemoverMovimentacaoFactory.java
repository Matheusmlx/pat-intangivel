package br.com.azi.patrimoniointangivel.configuration.factory.usecase.movimentacao;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.remover.RemoverMovimentacaoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.remover.RemoverMovimentacaoUseCaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RemoverMovimentacaoFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Bean("RemoverMovimentacaoUseCase")
    public RemoverMovimentacaoUseCase createUseCase(){
        return new RemoverMovimentacaoUseCaseImpl(movimentacaoDataProvider);
    }
}
