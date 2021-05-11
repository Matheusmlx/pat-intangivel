package br.com.azi.patrimoniointangivel.domain.gateway.dataprovider;

import br.com.azi.patrimoniointangivel.domain.entity.NotaLancamentoContabil;

public interface NotaLancamentoContabilDataProvider {

    boolean existePorNumero(String numeroNotaLancamento);

    NotaLancamentoContabil salvar(NotaLancamentoContabil notaLancamentoContabil);

    void remover(Long notaLancamentoContabilId);
}
