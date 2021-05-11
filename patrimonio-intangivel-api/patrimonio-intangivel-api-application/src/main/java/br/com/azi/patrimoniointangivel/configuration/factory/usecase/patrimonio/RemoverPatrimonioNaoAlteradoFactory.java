package br.com.azi.patrimoniointangivel.configuration.factory.usecase.patrimonio;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.remocao.RemoverPatrimonioUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.removernaoalterado.RemoverPatrimonioNaoAlteradoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.removernaoalterado.RemoverPatrimonioNaoAlteradoUseCaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class RemoverPatrimonioNaoAlteradoFactory {

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private RemoverPatrimonioUseCase removerPatrimonioUseCase;

    @Bean("RemoverPatrimonioNaoAlteradoUseCase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RemoverPatrimonioNaoAlteradoUseCase createUseCase() {
        return new RemoverPatrimonioNaoAlteradoUseCaseImpl(
            patrimonioDataProvider,
            removerPatrimonioUseCase
        );
    }
}
