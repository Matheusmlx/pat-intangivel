package br.com.azi.patrimoniointangivel.gateway.dataprovider.repository;


import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.RelatorioSinteticoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface RelatorioSinteticoRepository extends JpaRepository<RelatorioSinteticoEntity, Long>, QuerydslPredicateExecutor<RelatorioSinteticoEntity> {
    @Query("SELECT rs FROM RelatorioSinteticoEntity rs WHERE rs.orgaoAmortizacao.id = ?1 AND (rs.dataCadastro <= ?2 OR rs.dataCadastro is null) AND rs.dataAtivacao <= ?3")
    List<RelatorioSinteticoEntity> findRelatorio(Long orgao, LocalDateTime dataFinal, LocalDateTime data);

}
