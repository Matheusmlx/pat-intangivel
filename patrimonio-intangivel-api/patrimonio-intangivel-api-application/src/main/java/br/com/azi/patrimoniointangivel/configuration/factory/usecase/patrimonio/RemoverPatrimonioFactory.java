package br.com.azi.patrimoniointangivel.configuration.factory.usecase.patrimonio;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.remocao.RemoverPatrimonioUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.remocao.RemoverPatrimonioUseCaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class RemoverPatrimonioFactory {

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private DocumentoDataProvider documentoDataProvider;

    @Bean("RemoverPatrimonioUseCase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RemoverPatrimonioUseCase createUseCase() {
        return new RemoverPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            documentoDataProvider
        );
    }
}
