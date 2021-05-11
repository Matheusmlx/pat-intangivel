package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.editar;

import br.com.azi.patrimoniointangivel.domain.entity.ConfigContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.editar.converter.EditarContaContabilOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class EditarContaContabilUseCaseImpl implements EditarContaContabilUseCase {

    private ConfigContaContabilDataProvider configContaContabilDataProvider;

    private EditarContaContabilOutputDataConverter outputDataConverter;

    @Override
    public EditarContaContabilOutputData executar(EditarContaContabilInputData inputData) {
        validarDadosEntrada(inputData);

        ConfigContaContabil configAmortizacao = retornaConfigAmortizacao(inputData);
        ConfigContaContabil configAmortizacaoAtualizada = atualizar(configAmortizacao);

        return outputDataConverter.to(configAmortizacaoAtualizada);
    }

    private void validarDadosEntrada(EditarContaContabilInputData input) {
        Validator.of(input)
            .validate(EditarContaContabilInputData::getTipo, Objects::nonNull, "Tipo é nula")
            .get();
    }

    private ConfigContaContabil retornaConfigAmortizacao(EditarContaContabilInputData inputData) {
        return ConfigContaContabil
            .builder()
            .id(inputData.getId())
            .tipo(ConfigContaContabil.Tipo.valueOf(inputData.getTipo()))
            .metodo(retornaMetodoContaContabilPorTipo(ConfigContaContabil.Tipo.valueOf(inputData.getTipo())))
            .contaContabil(ContaContabil.builder().id(inputData.getContaContabil()).build())
            .build();
    }

    private ConfigContaContabil.Metodo retornaMetodoContaContabilPorTipo(ConfigContaContabil.Tipo tipo) {
        if (ConfigContaContabil.Tipo.AMORTIZAVEL.equals(tipo)) {
            return ConfigContaContabil.Metodo.QUOTAS_CONSTANTES;
        }
        return null;
    }

    private ConfigContaContabil atualizar(ConfigContaContabil configContaContabil) {
        return configContaContabilDataProvider.atualizar(configContaContabil);
    }
}
