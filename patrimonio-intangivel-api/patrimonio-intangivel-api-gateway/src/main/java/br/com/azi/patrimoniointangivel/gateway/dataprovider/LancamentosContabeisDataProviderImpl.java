package br.com.azi.patrimoniointangivel.gateway.dataprovider;

import br.com.azi.patrimoniointangivel.domain.entity.LancamentosContabeis;
import br.com.azi.patrimoniointangivel.domain.entity.LancamentosContabeis.MotivoLancamento;
import br.com.azi.patrimoniointangivel.domain.entity.LancamentosContabeisAgrupado;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.LancamentosContabeisDataProvider;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.converter.LancamentosContabeisAgrupadoConverter;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.converter.LancamentosContabeisConverter;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.LancamentosContabeisAgrupadoEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.LancamentosContabeisEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.repository.LancamentosContabeisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class LancamentosContabeisDataProviderImpl implements LancamentosContabeisDataProvider {

    @Autowired
    private LancamentosContabeisRepository repository;

    @Autowired
    private LancamentosContabeisAgrupadoConverter lancamentosContabeisAgrupadoConverter;

    @Autowired
    private LancamentosContabeisConverter converter;

    @Override
    @Modifying(flushAutomatically = true)
    @Transactional
    public LancamentosContabeis salvar(LancamentosContabeis lancamentosContabeis) {
        LancamentosContabeisEntity salva = repository.save(converter.from(lancamentosContabeis));
        return converter.to(salva);
    }

    @Override
    public Optional<LancamentosContabeis> buscarUltimoPorPatrimonioNoOrgaoAteDataReferencia(Long patrimonio, Long orgao, LocalDateTime dataReferencia) {
        Optional<LancamentosContabeisEntity> lancamentosContabeis = repository.findTopByPatrimonio_IdAndOrgao_IdAndTipoLancamentoEqualsAndDataLancamentoBeforeOrderByDataLancamentoDesc(patrimonio, orgao, "CREDITO", dataReferencia);
        return lancamentosContabeis.map(lancamentosContabeisEntity -> converter.to(lancamentosContabeisEntity));
    }

    @Override
    public Boolean validarSeUltimoLancamentoNoOrgaoCredito(Long patrimonio, Long orgao, LocalDateTime dataReferencia) {
        Optional<LancamentosContabeisEntity> lancamentosContabeis = repository.findFirstByPatrimonio_IdAndOrgao_IdAndDataLancamentoIsBeforeOrderByDataCadastroDesc(patrimonio, orgao, dataReferencia);
        return lancamentosContabeis.get().getTipoLancamento().equals("DEBITO");
    }

    @Override
    public  List<LancamentosContabeis> buscarCreditosNoOrgaoAteData(Long orgao, LocalDateTime dataReferencia) {
        List<LancamentosContabeisEntity> lancamentosContabeis = repository.buscarCreditosNoOrgaoAteData(orgao, dataReferencia);
        return lancamentosContabeis
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());
    }



    @Override
    public List<LancamentosContabeisAgrupado> buscarLancamentosContabeisAgrupados(LocalDateTime data) {
        List<LancamentosContabeisAgrupadoEntity> results = repository.buscarLancamentoContabeisAgrupados(data);

        return results
            .stream()
            .map(lancamentosContabeisAgrupadoConverter::to)
            .collect(Collectors.toList());
    }

    @Override
    public List<LancamentosContabeisAgrupado> buscarLancamentosContabeisAgrupadosPorOrgao(LocalDateTime dataFinal, Long orgao) {
        List<LancamentosContabeisAgrupadoEntity> results = repository.buscarLancamentoContabeisAgrupadosNoOrgao(dataFinal, orgao);

        return results
            .stream()
            .map(lancamentosContabeisAgrupadoConverter::to)
            .collect(Collectors.toList());
    }


    @Override
    public Optional<LancamentosContabeis> buscarPorMovimentacaoETipoLancamento(Long movimentacaoId, String tipoLancamento){
        Optional<LancamentosContabeisEntity> encontrada = repository.findByMovimentacaoIdAndTipoLancamento(movimentacaoId, tipoLancamento);
        return encontrada.map(converter::to);
    }

    @Override
    @Transactional
    public void excluirPorPatrimonio(Long patrimonio) {
        repository.removeAllByPatrimonio_Id(patrimonio);
    }

    @Override
    public Optional<LancamentosContabeis> buscarLancamentoContabilAnteriorCredito(Long patrimonioId, Long orgaoId, LocalDateTime dataReferencia){
        Optional<LancamentosContabeisEntity> lancamentosContabeis = repository.findFirstByPatrimonio_IdAndOrgao_IdAndDataLancamentoIsBeforeOrderByDataCadastroDesc(patrimonioId, orgaoId, dataReferencia);
        return lancamentosContabeis.map(converter::to);
    }

    @Override
    public Optional<LancamentosContabeis> buscarLancamentoContabilDeAtivacaoDoPatrimonio(Long patrimonioId) {
        Optional<LancamentosContabeisEntity> lancamentosContabeis = repository.findFirstByPatrimonio_idAndMotivoLancamentoOrderByIdAsc(patrimonioId, MotivoLancamento.ATIVACAO.toString());
        return lancamentosContabeis.map(converter::to);
    }
}
