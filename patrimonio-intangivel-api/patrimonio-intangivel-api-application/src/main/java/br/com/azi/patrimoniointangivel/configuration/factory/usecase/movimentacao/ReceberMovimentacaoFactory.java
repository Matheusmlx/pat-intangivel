package br.com.azi.patrimoniointangivel.configuration.factory.usecase.movimentacao;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.LancamentosContabeisDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.UnidadeOrganizacionalDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.UsuarioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.receber.ReceberMovimentacaoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.receber.ReceberMovimentacaoUseCaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.time.Clock;

@Configuration
public class ReceberMovimentacaoFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private UnidadeOrganizacionalDataProvider unidadeOrganizacionalDataProvider;

    @Autowired
    private UsuarioDataProvider usuarioDataProvider;

    @Autowired
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Autowired
    private LancamentosContabeisDataProvider lancamentosContabeisDataProvider;

    @Bean("ReceberMovimentacaoUseCase")
    @DependsOn({"Clock"})
    public ReceberMovimentacaoUseCase createUseCase(Clock clock){
        return new ReceberMovimentacaoUseCaseImpl(movimentacaoDataProvider, patrimonioDataProvider,unidadeOrganizacionalDataProvider, usuarioDataProvider, sessaoUsuarioDataProvider, lancamentosContabeisDataProvider, clock);
    }
}
