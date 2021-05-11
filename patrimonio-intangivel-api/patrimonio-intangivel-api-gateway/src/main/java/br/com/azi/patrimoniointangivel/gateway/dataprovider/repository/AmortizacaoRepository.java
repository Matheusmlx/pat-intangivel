package br.com.azi.patrimoniointangivel.gateway.dataprovider.repository;

import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.AmortizacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AmortizacaoRepository extends JpaRepository<AmortizacaoEntity, Long> {

    Boolean existsByPatrimonio_Id(Long patrimonioId);

    List<AmortizacaoEntity> findByPatrimonio_IdOrderByDataFinal(Long patrimonioId);

    Optional<AmortizacaoEntity> findFirstByPatrimonio_IdOrderByDataFinalDesc(Long patrimonioId);

    Optional<AmortizacaoEntity> findFirstByOrgaoIdAndPatrimonioIdOrderByDataFinalAsc(Long orgaoId, Long patrimonioId);

    Optional<AmortizacaoEntity> findFirstByPatrimonio_IdAndDataFinalIsLessThanEqualOrderByDataFinalDesc(Long patrimonioId, Date dataReferencia);

    Boolean existsByPatrimonio_IdAndDataFinalLessThanEqual(Long patrimonioId, Date dataReferencia);

    Optional<AmortizacaoEntity> findFirstByPatrimonio_IdAndDataFinalLessThanEqualOrderByDataFinalDesc(Long patrimonioId, Date dataReferencia);

}
