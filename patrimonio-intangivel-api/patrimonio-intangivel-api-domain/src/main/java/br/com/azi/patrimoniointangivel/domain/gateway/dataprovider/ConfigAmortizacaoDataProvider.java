package br.com.azi.patrimoniointangivel.domain.gateway.dataprovider;

import br.com.azi.patrimoniointangivel.domain.entity.ConfigAmortizacao;

import java.util.Optional;

public interface ConfigAmortizacaoDataProvider {

    ConfigAmortizacao salvar(ConfigAmortizacao configAmortizacao);

    ConfigAmortizacao atualizar(ConfigAmortizacao configAmortizacao);

    Optional<ConfigAmortizacao> buscarPorId(Long id);

    Optional<ConfigAmortizacao> buscarAtualPorContaContabil(Long contaContabilId);

    boolean existe(Long id);

    void remover(Long id);
}
