package br.com.azi.patrimoniointangivel.domain.gateway.dataprovider;

import br.com.azi.patrimoniointangivel.domain.entity.LancamentosContabeis;
import br.com.azi.patrimoniointangivel.domain.entity.LancamentosContabeisAgrupado;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LancamentosContabeisDataProvider {

    LancamentosContabeis salvar(LancamentosContabeis lancamentosContabeis);

    Optional<LancamentosContabeis> buscarUltimoPorPatrimonioNoOrgaoAteDataReferencia(Long patrimonio, Long orgao, LocalDateTime dataReferencia);

    Optional<LancamentosContabeis> buscarLancamentoContabilAnteriorCredito(Long patrimonioId, Long orgaoId, LocalDateTime dataReferencia);

    Boolean validarSeUltimoLancamentoNoOrgaoCredito(Long patrimonio, Long orgao, LocalDateTime dataReferencia);

    List<LancamentosContabeis> buscarCreditosNoOrgaoAteData(Long orgao, LocalDateTime dataReferencia);

    List<LancamentosContabeisAgrupado> buscarLancamentosContabeisAgrupados(LocalDateTime dataFinal);

    List<LancamentosContabeisAgrupado> buscarLancamentosContabeisAgrupadosPorOrgao(LocalDateTime dataFinal, Long orgao);

    Optional<LancamentosContabeis> buscarPorMovimentacaoETipoLancamento(Long movimentacaoId, String tipoLancamento);

    void excluirPorPatrimonio(Long patrimonio);

    Optional<LancamentosContabeis> buscarLancamentoContabilDeAtivacaoDoPatrimonio(Long patrimonioId);
}
