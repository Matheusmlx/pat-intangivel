package br.com.azi.patrimoniointangivel.gateway.dataprovider.repository;

import br.com.azi.patrimoniointangivel.domain.entity.Documento;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.DocumentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface DocumentoRepository extends JpaRepository<DocumentoEntity, Long>, QuerydslPredicateExecutor<Documento> {

    List<DocumentoEntity> findAllByPatrimonio_Id(Long id);

    List<DocumentoEntity> findAllByMovimentacao_Id(Long idMovimentacao);

    boolean existsByPatrimonio_IdAndNumeroAndTipoDocumento_Id(Long id, String numero, Long tipo);

    boolean existsByMovimentacao_IdAndNumeroAndTipoDocumento_Id(Long id, String numero, Long tipo);

    Optional<DocumentoEntity> findByPatrimonio_IdAndNumero(Long id, String numero);

    Long countByPatrimonio_Id(Long patrimonioId);

    Long countByMovimentacao_Id(Long movimentacaoId);

}
