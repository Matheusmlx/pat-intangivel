package br.com.azi.patrimoniointangivel.gateway.dataprovider.repository;

import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.HistoricoMemorandoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoMemorandoRepository extends JpaRepository<HistoricoMemorandoEntity, Long>, QuerydslPredicateExecutor<HistoricoMemorandoEntity> {
}
