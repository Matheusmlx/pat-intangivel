package br.com.azi.patrimoniointangivel.gateway.integration.hal;

import br.com.azi.patrimoniointangivel.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeConfiguracoesIntegration;
import br.com.azi.patrimoniointangivel.gateway.integration.hal.entity.HalIntegrationProperties;
import br.com.azi.patrimoniointangivel.gateway.integration.hal.usecase.alterarpropriedade.AlterarPropriedadeIntegrationUseCase;
import br.com.azi.patrimoniointangivel.gateway.integration.hal.usecase.autenticar.AutenticarIntegrationUseCase;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SistemaDeConfiguracoesIntegrationHalImpl implements SistemaDeConfiguracoesIntegration {

    private HalIntegrationProperties integrationProperties;

    private AutenticarIntegrationUseCase autenticarIntegrationUseCase;

    private AlterarPropriedadeIntegrationUseCase alterarPropriedadeIntegrationUseCase;

    @Override
    public void alterarPropriedade(String nome, String valor) {
        SessaoUsuario sessaoUsuario = autenticarIntegrationUseCase.executar(integrationProperties);
        alterarPropriedadeIntegrationUseCase.executar(integrationProperties, sessaoUsuario, nome, valor);
    }
}
