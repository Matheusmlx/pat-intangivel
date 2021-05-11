package br.com.azi.patrimoniointangivel.gateway.integration.hal.usecase.unidadeorganizacional.buscarunidadesorganizacionais.converter;

import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.gateway.integration.hal.usecase.unidadeorganizacional.buscarunidadesorganizacionais.entity.BuscarUnidadesOrganizacionaisIntegrationResponse;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BuscarUnidadesOrganizacionaisIntegrationConverter {

    private static final BuscarUnidadesOrganizacionaisIntegrationConverter.ItemOutputDataConverter ITEM_OUTPUT_DATA_CONVERTER = new BuscarUnidadesOrganizacionaisIntegrationConverter.ItemOutputDataConverter();

    public List<UnidadeOrganizacional> to(BuscarUnidadesOrganizacionaisIntegrationResponse source) {
        return source.getContent()
            .stream()
            .map(this::converterItem)
            .collect(Collectors.toList());
    }

    private UnidadeOrganizacional converterItem(BuscarUnidadesOrganizacionaisIntegrationResponse.EstruturaOrganizacional source) {
        UnidadeOrganizacional target = ITEM_OUTPUT_DATA_CONVERTER.to(source);

        target.setSigla(source.getSigla());
        target.setCodHierarquia(source.getCodigoHierarquia());
        target.setId(source.getId());
        target.setNome(source.getNome());
        target.setDescricao(String.format("%s - %s", source.getSigla(), source.getNome()));

        return target;
    }

    private static class ItemOutputDataConverter extends GenericConverter<BuscarUnidadesOrganizacionaisIntegrationResponse.EstruturaOrganizacional, UnidadeOrganizacional> {
    }

}
