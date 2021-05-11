package br.com.azi.patrimoniointangivel.gateway.dataprovider;

import br.com.azi.patrimoniointangivel.domain.entity.HistoricoMemorando;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.HistoricoMemorandoDataProvider;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.converter.HistoricoMemorandoConverter;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.HistoricoMemorandoEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.repository.HistoricoMemorandoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class HistoricoMemorandoDataProviderImpl implements HistoricoMemorandoDataProvider {

    @Autowired
    private HistoricoMemorandoRepository repository;

    @Autowired
    private HistoricoMemorandoConverter converter;

    @Override
    @Transactional
    public HistoricoMemorando salvar(HistoricoMemorando memorando) {
        HistoricoMemorandoEntity salva = repository.save(converter.from(memorando));
        return converter.to(salva);
    }
}
