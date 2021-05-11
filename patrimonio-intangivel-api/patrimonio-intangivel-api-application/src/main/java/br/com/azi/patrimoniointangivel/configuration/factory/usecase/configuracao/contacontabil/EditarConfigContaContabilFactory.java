package br.com.azi.patrimoniointangivel.configuration.factory.usecase.configuracao.contacontabil;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.editar.EditarContaContabilUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.editar.EditarContaContabilUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.editar.converter.EditarContaContabilOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class EditarConfigContaContabilFactory {

    @Autowired
    private ConfigContaContabilDataProvider configContaContabilDataProvider;

    @Bean("EditarConfigContaContabilUseCase")
    @DependsOn({"EditarConfigContaContabilOutputDataConverter"})
    public EditarContaContabilUseCase createUseCase(EditarContaContabilOutputDataConverter outputDataConverter) {
        return new EditarContaContabilUseCaseImpl(configContaContabilDataProvider, outputDataConverter);
    }

    @Bean("EditarConfigContaContabilOutputDataConverter")
    public EditarContaContabilOutputDataConverter createConverter() {
        return new EditarContaContabilOutputDataConverter();
    }
}
