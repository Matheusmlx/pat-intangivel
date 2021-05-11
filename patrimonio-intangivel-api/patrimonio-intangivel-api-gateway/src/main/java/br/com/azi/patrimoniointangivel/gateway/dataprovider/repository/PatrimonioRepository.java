package br.com.azi.patrimoniointangivel.gateway.dataprovider.repository;

import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.PatrimonioEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.PatrimoniosAgrupadosEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatrimonioRepository extends JpaRepository<PatrimonioEntity, Long>, QuerydslPredicateExecutor<PatrimonioEntity> {

    Optional<PatrimonioEntity> findFirstByNumeroNotNullOrderByNumeroDesc();

    List<PatrimonioEntity> findBySituacaoEqualsAndValorLiquidoIsAfterAndAmortizavelIsTrue(String situacao, BigDecimal valorResidual);

    List<PatrimonioEntity> findBySituacaoEqualsAndValorLiquidoIsAfterAndOrgao_IdEqualsAndAmortizavelIsTrue(String situacao, BigDecimal valorResidual, Long orgao);

    Optional<PatrimonioEntity> findFirstByNumeroMemorandoNotNullOrderByNumeroMemorandoDesc();

    Long countAllByOrgao_IdInOrOrgaoIsNull(List<Long> ids);

    Long countAllBySituacaoEqualsAndOrgao_IdInOrOrgaoIsNull(String situacao, List<Long> ids);

    Long countAllBySituacaoEqualsAndOrgao_IdIn(String situacao, List<Long> ids);

    @Query("SELECT COUNT(PA.id) FROM PatrimonioEntity PA WHERE PA.tipo = ?1 AND (PA.orgao.id in (?2) OR PA.orgao.id is null) ")
    Long countAllByTipoEqualsAndOrgao_IdInOrOrgaoIsNull(String tipo, List<Long> ids);

    Page<PatrimonioEntity> findAllBySituacaoEqualsAndOrgao_IdInAndFimVidaUtilIsGreaterThanEqualOrderByFimVidaUtilAsc(String situacao, List<Long> idsOrgao,
                                                                                                                     Date dataAtual, Pageable pageable);

    @Query("SELECT new PatrimonioEntity(PA.id) FROM" +
        " PatrimonioEntity PA" +
        " WHERE PA.amortizavel = TRUE" +
        " AND PA.situacao = 'ATIVO'" +
        " AND PA.valorDeEntrada = PA.valorLiquido" +
        " AND PA.orgao.id = ?1 ")
    List<PatrimonioEntity> validarSeExistePatrimonioNaoAmortizadoNoOrgao(Long orgao);

    @Query("SELECT " +
        " PA.orgao.id as idOrgao, " +
        " PA.tipo as tipo,  " +
        " count(PA.tipo) as contador " +
        " FROM PatrimonioEntity PA " +
        " WHERE PA.orgao.id in ( :orgaos ) " +
        " group by PA.orgao.id , PA.tipo ")
    List<PatrimoniosAgrupadosEntity> buscarPatrimoniosAgrupadosPorOrgaoETipo(@Param(value = "orgaos") List<Long> orgaos);
}
