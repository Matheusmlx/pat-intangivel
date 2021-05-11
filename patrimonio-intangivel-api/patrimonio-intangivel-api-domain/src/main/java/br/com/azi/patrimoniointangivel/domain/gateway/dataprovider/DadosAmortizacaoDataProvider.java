package br.com.azi.patrimoniointangivel.domain.gateway.dataprovider;

import br.com.azi.patrimoniointangivel.domain.entity.DadosAmortizacao;

import java.util.Optional;

public interface DadosAmortizacaoDataProvider {

    DadosAmortizacao salvar(DadosAmortizacao dadosAmortizacao);

    Optional<DadosAmortizacao> buscarPorId(Long id);

    boolean existe(Long id);

    void remover(Long id);

}
