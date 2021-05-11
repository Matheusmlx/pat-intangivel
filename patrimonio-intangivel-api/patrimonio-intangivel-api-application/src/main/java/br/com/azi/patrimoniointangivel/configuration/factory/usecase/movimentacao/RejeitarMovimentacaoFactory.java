package br.com.azi.patrimoniointangivel.configuration.factory.usecase.movimentacao;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.UsuarioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.rejeitar.RejeitarMovimentacaoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.rejeitar.RejeitarMovimentacaoUseCaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.time.Clock;

@Configuration
public class RejeitarMovimentacaoFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private UsuarioDataProvider usuarioDataProvider;

    @Autowired
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Bean("RejeitarMovimentacaoUseCase")
    @DependsOn({"Clock"})
    public RejeitarMovimentacaoUseCase createUseCase(Clock clock){
        return new RejeitarMovimentacaoUseCaseImpl(movimentacaoDataProvider, usuarioDataProvider, sessaoUsuarioDataProvider, clock);
    }

}
