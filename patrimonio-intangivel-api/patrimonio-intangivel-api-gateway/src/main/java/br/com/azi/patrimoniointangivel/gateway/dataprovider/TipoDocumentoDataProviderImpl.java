package br.com.azi.patrimoniointangivel.gateway.dataprovider;

import br.com.azi.patrimoniointangivel.domain.entity.TipoDocumento;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.TipoDocumentoDataprovider;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.converter.TipoDocumentoConverter;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.TipoDocumentoEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.repository.TipoDocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TipoDocumentoDataProviderImpl implements TipoDocumentoDataprovider {

    @Autowired
    private TipoDocumentoRepository repository;

    @Autowired
    private TipoDocumentoConverter converter;

    @Override
    public List<TipoDocumento> buscar() {
        List<TipoDocumentoEntity> entidadesEncontradas = repository.findAll();

        return entidadesEncontradas
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<TipoDocumento> buscarPorId(Long id) {
        Optional<TipoDocumentoEntity> encontrada = repository.findById(id);
        return encontrada.map(tipoDocumentoEntity -> converter.to(tipoDocumentoEntity));
    }
}
