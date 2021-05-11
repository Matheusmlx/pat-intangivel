package br.com.azi.patrimoniointangivel.gateway.dataprovider.repository;

import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.ConfigAmortizacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigAmortizacaoRepository extends JpaRepository<ConfigAmortizacaoEntity, Long> {

    Optional<ConfigAmortizacaoEntity> findFirstByContaContabil_idAndSituacaoOrderByDataCadastroDesc(Long contaContabilId, String situacao);

}
