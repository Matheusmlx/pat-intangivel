package br.com.azi.patrimoniointangivel.gateway.dataprovider;

import br.com.azi.patrimoniointangivel.domain.entity.RelatorioSintetico;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.RelatorioSinteticoDataProvider;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.converter.RelatorioSinteticoConverter;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.RelatorioSinteticoEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.repository.RelatorioSinteticoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RelatorioSinteticoDataProviderImpl implements RelatorioSinteticoDataProvider {

    @Autowired
    private RelatorioSinteticoRepository repository;


    @Autowired
    private RelatorioSinteticoConverter converter;

    @Override
    public List<RelatorioSintetico> buscaRelatorioSintetico(Long orgao, LocalDateTime dataFinal) {
        List<RelatorioSinteticoEntity> results = repository.findRelatorio(orgao, dataFinal, dataFinal);

        return results
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());
    }
}
