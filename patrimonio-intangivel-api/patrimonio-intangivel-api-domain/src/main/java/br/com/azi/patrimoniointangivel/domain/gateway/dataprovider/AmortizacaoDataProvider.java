package br.com.azi.patrimoniointangivel.domain.gateway.dataprovider;


import br.com.azi.patrimoniointangivel.domain.entity.Amortizacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AmortizacaoDataProvider {

    Boolean existePorPatrimonio(Long patrimonioId);

    Amortizacao salvar(Amortizacao amortizacao);

    Optional<Amortizacao> buscarUltimaPorPatrimonio(Long patrimonioId);

    Optional<Amortizacao> buscarPrimeiraPorOrgaoEPatrimonio(Long orgaoId, Long patrimonioId);

    List<Amortizacao> buscar(Long patrimonioId);

    BigDecimal buscarValorSubtraidoPorPatrimonioId(Long patrimonioId);

    Boolean existePorPatrimonioNoPeriodo(Long patrimonioId, LocalDateTime mesReferencia);

    Boolean existePorPatrimonioEOrgaoNoPeriodo(Long patrimonioId, Long orgaoId, LocalDateTime mesReferencia);

    Optional<Amortizacao> buscarPorPatrimonioEDataLimite(Long patrimonioId, LocalDateTime dataReferencia);

    Boolean existePorPatrimonioAteDataLimite(Long patrimonioId, LocalDateTime mesReferencia);
}
