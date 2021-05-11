package br.com.azi.patrimoniointangivel.domain.gateway.dataprovider;

import br.com.azi.patrimoniointangivel.domain.entity.ListaPaginada;
import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;

import java.util.Optional;

public interface MovimentacaoDataProvider {

    Movimentacao salvar(Movimentacao movimentacao);

    boolean existe(Long id);

    Optional<Movimentacao> buscarPorId(Long id);

    Optional<Movimentacao> buscarUltimoCriado();

    void remover(Long id);

    ListaPaginada<Movimentacao> buscarMovimentacoesPorPatrimonio(Long idPatrimonio, Long syze);

    ListaPaginada<Movimentacao> buscarPorFiltro(Movimentacao.Filtro filtro);

    boolean existePorIdPatrimonio(Long idPatrimonio);
}
