package br.com.azi.patrimoniointangivel.gateway.dataprovider;


import br.com.azi.patrimoniointangivel.domain.entity.ConfigAmortizacao;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigAmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.converter.ConfigAmortizacaoConverter;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.ConfigAmortizacaoEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.repository.ConfigAmortizacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Optional;

@Component
public class ConfigAmortizacaoDataProviderImpl implements ConfigAmortizacaoDataProvider {

    @Autowired
    private ConfigAmortizacaoRepository repository;

    @Autowired
    private ConfigAmortizacaoConverter converter;

    @Override
    public ConfigAmortizacao salvar(ConfigAmortizacao business) {
        ConfigAmortizacaoEntity salva = repository.save(converter.from(business));
        return converter.to(salva);
    }

    @Override
    @Transactional
    public ConfigAmortizacao atualizar(ConfigAmortizacao business) {
        ConfigAmortizacaoEntity salva = repository.save(converter.from(business));
        return converter.to(salva);
    }

    @Override
    @Transactional
    public Optional<ConfigAmortizacao> buscarPorId(Long id) {
        Optional<ConfigAmortizacaoEntity> encontrada = repository.findById(id);
        return encontrada.map(converter::to);
    }

    @Override
    @Transactional
    public void remover(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<ConfigAmortizacao> buscarAtualPorContaContabil(Long contaContabilId) {
        Optional<ConfigAmortizacaoEntity> encontrada = repository.findFirstByContaContabil_idAndSituacaoOrderByDataCadastroDesc(contaContabilId, ConfigAmortizacao.Situacao.ATIVO.name());
        return encontrada.map(converter::to);
    }

    @Override
    public boolean existe(Long id) {
        return repository.existsById(id);
    }

}
