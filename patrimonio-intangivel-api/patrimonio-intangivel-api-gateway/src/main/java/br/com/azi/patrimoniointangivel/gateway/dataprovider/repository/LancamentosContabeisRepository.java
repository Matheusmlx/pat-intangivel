package br.com.azi.patrimoniointangivel.gateway.dataprovider.repository;

import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.LancamentosContabeisAgrupadoEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.LancamentosContabeisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LancamentosContabeisRepository extends JpaRepository<LancamentosContabeisEntity, Long> {

    @Query("SELECT new br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.LancamentosContabeisAgrupadoEntity(" +
        "   lc.patrimonio, " +
        "   (select subLc.orgao FROM LancamentosContabeisEntity subLc where subLc.patrimonio.id = lc.patrimonio.id and subLc.dataLancamento = max(lc.dataLancamento) and subLc.tipoLancamento = 'CREDITO'), " +
        "   max(lc.dataLancamento), " +
        "   (select subLc.valorLiquido FROM LancamentosContabeisEntity subLc where subLc.patrimonio.id = lc.patrimonio.id and subLc.dataLancamento = max(lc.dataLancamento) and subLc.tipoLancamento = 'CREDITO')" +
        " ) " +
        " FROM LancamentosContabeisEntity lc " +
        " where lc.dataLancamento <= ?1 " +
        " and lc.tipoLancamento = 'CREDITO' " +
        " group by lc.patrimonio")
    List<LancamentosContabeisAgrupadoEntity> buscarLancamentoContabeisAgrupados(LocalDateTime data);

    @Query("SELECT new br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.LancamentosContabeisAgrupadoEntity(" +
        "   lc.patrimonio, " +
        "   (select subLc.orgao FROM LancamentosContabeisEntity subLc where subLc.patrimonio.id = lc.patrimonio.id and subLc.dataLancamento = max(lc.dataLancamento) and subLc.tipoLancamento = 'CREDITO'), " +
        "   max(lc.dataLancamento), " +
        "   (select subLc.valorLiquido FROM LancamentosContabeisEntity subLc where subLc.patrimonio.id = lc.patrimonio.id and subLc.dataLancamento = max(lc.dataLancamento) and subLc.tipoLancamento = 'CREDITO')" +
        " ) " +
        " FROM LancamentosContabeisEntity lc " +
        " where lc.dataLancamento <= ?1 " +
        " and lc.tipoLancamento = 'CREDITO'" +
        " and lc.orgao.id = ?2 " +
        " group by lc.patrimonio")
    List<LancamentosContabeisAgrupadoEntity> buscarLancamentoContabeisAgrupadosNoOrgao(LocalDateTime data, Long orgao);

    Optional<LancamentosContabeisEntity> findFirstByPatrimonio_idAndMotivoLancamentoOrderByIdAsc(Long patrimonioId, String motivoLancamento);

    Optional<LancamentosContabeisEntity> findByMovimentacaoIdAndTipoLancamento(Long movimentacaoId, String TipoLancamento);

    Optional<LancamentosContabeisEntity> findTopByPatrimonio_IdAndOrgao_IdAndTipoLancamentoEqualsAndDataLancamentoBeforeOrderByDataLancamentoDesc(Long patrimonio, Long orgao, String tipoLancamento, LocalDateTime dataInicial);

    Optional<LancamentosContabeisEntity> findFirstByPatrimonio_IdAndOrgao_IdAndDataLancamentoIsBeforeOrderByDataCadastroDesc(Long patrimonio, Long orgao, LocalDateTime data);

    @Query("SELECT LC " +
        "       from LancamentosContabeisEntity LC " +
        "   where LC.orgao.id = ?1 " +
        "   AND   LC.tipoLancamento = 'CREDITO' " +
        "   AND   LC.dataLancamento < ?2 ")
    List<LancamentosContabeisEntity> buscarCreditosNoOrgaoAteData(Long orgao, LocalDateTime data);

    void removeAllByPatrimonio_Id(Long patrimonio);
}
