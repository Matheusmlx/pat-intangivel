package br.com.azi.patrimoniointangivel.gateway.dataprovider.repository;

import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.NotaLancamentoContabilEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaLancamentoContabilRepository extends JpaRepository<NotaLancamentoContabilEntity, Long> {

    boolean existsByNumero(String numero);
}
