package br.com.azi.patrimoniointangivel.gateway.dataprovider.repository;

import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.ConfigContaContabilEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfigContaContabilRepository extends JpaRepository<ConfigContaContabilEntity, Long> {

    Optional<ConfigContaContabilEntity> findFirstByContaContabil_idOrderByDataCadastroDesc(Long contaContabilId);
}
