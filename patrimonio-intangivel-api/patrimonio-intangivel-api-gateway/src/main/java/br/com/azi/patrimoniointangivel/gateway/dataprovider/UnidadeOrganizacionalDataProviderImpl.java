package br.com.azi.patrimoniointangivel.gateway.dataprovider;

import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.UnidadeOrganizacionalDataProvider;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.converter.UnidadeOrganizacionalConverter;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.UnidadeOrganizacionalEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.repository.UnidadeOrganizacionalReadOnlyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UnidadeOrganizacionalDataProviderImpl implements UnidadeOrganizacionalDataProvider {

    @Autowired
    private UnidadeOrganizacionalReadOnlyRepository repository;

    @Autowired
    private UnidadeOrganizacionalConverter converter;

    @Override
    public Optional<UnidadeOrganizacional> buscarPorId(Long id) {
        Optional<UnidadeOrganizacionalEntity> encontrada = repository.findById(id);
        return encontrada.map(entity -> converter.to(entity));
    }
}
