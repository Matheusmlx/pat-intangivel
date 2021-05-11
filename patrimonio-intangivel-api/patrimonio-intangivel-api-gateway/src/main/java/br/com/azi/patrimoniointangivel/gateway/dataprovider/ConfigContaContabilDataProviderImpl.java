package br.com.azi.patrimoniointangivel.gateway.dataprovider;

import br.com.azi.patrimoniointangivel.domain.entity.ConfigContaContabil;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.converter.ConfigContaContabilConverter;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.ConfigContaContabilEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.repository.ConfigContaContabilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Optional;

@Component
public class ConfigContaContabilDataProviderImpl implements ConfigContaContabilDataProvider {


    @Autowired
    private ConfigContaContabilRepository repository;

    @Autowired
    private ConfigContaContabilConverter converter;

    @Override
    public ConfigContaContabil salvar(ConfigContaContabil business) {
        ConfigContaContabilEntity salva = repository.save(converter.from(business));
        return converter.to(salva);
    }

    @Override
    @Transactional
    public ConfigContaContabil atualizar(ConfigContaContabil business) {
        ConfigContaContabilEntity salva = repository.save(converter.from(business));
        return converter.to(salva);
    }

    @Override
    @Transactional
    public Optional<ConfigContaContabil> buscarPorId(Long id) {
        Optional<ConfigContaContabilEntity> encontrada = repository.findById(id);
        return encontrada.map(converter::to);
    }

    @Override
    public Optional<ConfigContaContabil> buscarAtualPorContaContabil(Long contaContabilId) {
        Optional<ConfigContaContabilEntity> encontrada = repository.findFirstByContaContabil_idOrderByDataCadastroDesc(contaContabilId);
        return encontrada.map(converter::to);
    }

    @Override
    public boolean existe(Long id) {
        return repository.existsById(id);
    }
}




