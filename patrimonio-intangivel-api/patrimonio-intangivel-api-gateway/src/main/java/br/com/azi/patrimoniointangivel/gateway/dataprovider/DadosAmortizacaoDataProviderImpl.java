package br.com.azi.patrimoniointangivel.gateway.dataprovider;

import br.com.azi.patrimoniointangivel.domain.entity.DadosAmortizacao;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DadosAmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.converter.DadosAmortizacaoConverter;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.DadosAmortizacaoEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.repository.DadosAmortizacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Optional;

@Component
public class DadosAmortizacaoDataProviderImpl implements DadosAmortizacaoDataProvider {

    @Autowired
    DadosAmortizacaoRepository repository;

    @Autowired
    DadosAmortizacaoConverter converter;

    @Override
    public DadosAmortizacao salvar(DadosAmortizacao dadosAmortizacao) {
        DadosAmortizacaoEntity salva = repository.save(converter.from(dadosAmortizacao));
        return converter.to(salva);
    }

    @Override
    public boolean existe(Long id) {
        return repository.existsById(id);
    }

    @Override
    @Transactional
    public Optional<DadosAmortizacao> buscarPorId(Long id) {
        Optional<DadosAmortizacaoEntity> encontrada = repository.findById(id);
        return encontrada.map(dadosAmortizacaoEntity -> converter.to(dadosAmortizacaoEntity));
    }

    @Override
    public void remover(Long id) {
        repository.deleteById(id);
    }
}
