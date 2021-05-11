package br.com.azi.patrimoniointangivel.domain.usecase.unidadeorganizacional.buscarfilhos.converter;


import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.usecase.unidadeorganizacional.buscarfilhos.BuscarUnidadeOrganizacionalPorIdOrgaoOutputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;

import java.util.List;
import java.util.stream.Collectors;


public class BuscarUnidadeOrganizacionalPorIdOutputDataConverter {

    private static final ItemOutputDataConverter ITEM_OUTPUT_DATA_CONVERTER = new ItemOutputDataConverter();

    public BuscarUnidadeOrganizacionalPorIdOrgaoOutputData to(List<UnidadeOrganizacional> source) {
        List<BuscarUnidadeOrganizacionalPorIdOrgaoOutputData.UnidadeOrganizacional> itens = source.stream()
            .map(this::converterItem)
            .collect(Collectors.toList());

        BuscarUnidadeOrganizacionalPorIdOrgaoOutputData target = new BuscarUnidadeOrganizacionalPorIdOrgaoOutputData();
        target.setItems(itens);

        return target;
    }

    private BuscarUnidadeOrganizacionalPorIdOrgaoOutputData.UnidadeOrganizacional converterItem(UnidadeOrganizacional source) {
        return ITEM_OUTPUT_DATA_CONVERTER.to(source);
    }

    private static class ItemOutputDataConverter extends GenericConverter<UnidadeOrganizacional, BuscarUnidadeOrganizacionalPorIdOrgaoOutputData.UnidadeOrganizacional> {
    }
}
