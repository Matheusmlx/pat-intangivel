package br.com.azi.patrimoniointangivel.domain.usecase.unidadeorganizacional.buscarorgao.converter;


import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.usecase.unidadeorganizacional.buscarorgao.BuscarUnidadesOrganizacionaisOutputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;

import java.util.List;
import java.util.stream.Collectors;


public class BuscarUnidadesOrganizacionaisOutputDataConverter {

    private static final ItemOutputDataConverter ITEM_OUTPUT_DATA_CONVERTER = new ItemOutputDataConverter();

    public BuscarUnidadesOrganizacionaisOutputData to(List<UnidadeOrganizacional> source) {
        List<BuscarUnidadesOrganizacionaisOutputData.UnidadeOrganizacional> itens = source.stream()
            .map(this::converterItem)
            .collect(Collectors.toList());

        BuscarUnidadesOrganizacionaisOutputData target = new BuscarUnidadesOrganizacionaisOutputData();
        target.setItems(itens);

        return target;
    }

    private BuscarUnidadesOrganizacionaisOutputData.UnidadeOrganizacional converterItem(UnidadeOrganizacional source) {
        return ITEM_OUTPUT_DATA_CONVERTER.to(source);
    }

    private static class ItemOutputDataConverter extends GenericConverter<UnidadeOrganizacional, BuscarUnidadesOrganizacionaisOutputData.UnidadeOrganizacional> {
    }

}
