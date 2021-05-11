package br.com.azi.patrimoniointangivel.domain.gateway.dataprovider;

import br.com.azi.patrimoniointangivel.domain.entity.ListaPaginada;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.PatrimoniosAgrupados;

import java.util.List;
import java.util.Optional;

public interface PatrimonioDataProvider {

    Patrimonio salvar(Patrimonio patrimonio);

    Optional<Patrimonio> buscarPorId(Long id);

    ListaPaginada<Patrimonio> buscarPorFiltro(Patrimonio.Filtro filtro);

    Patrimonio atualizar(Patrimonio patrimonio);

    void remover(Long id);

    boolean existe(Long id);

    Optional<Patrimonio> buscarUltimoAtivado();

    List<Patrimonio> buscarPatrimoniosAmortizaveis();

    List<Patrimonio> buscarPatrimoniosAmortizaveisPorOrgao(Long orgaoId);

    Optional<Patrimonio> buscarUltimoPorMemorando();

    Long contarTotalDeRegistrosPorOrgaos(List<Long> idsOrgao);

    Long contarEmElaboracaoPorOrgaos(List<Long> idsOrgao);

    Long contarAtivosPorOrgaos(List<Long> idsOrgao);

    Long contarPorTipoEOrgaos(String tipo, List<Long> idsOrgao);

    List<Patrimonio> buscarProximosPatrimoniosAVencerNosOrgaos(List<Long> idsOrgao, Long numeroDeRegistrosRetornados);

    List<PatrimoniosAgrupados> buscarPatrimoniosAgrupadosPorOrgaoETipo(List<Long> idsOrgao);
}
