package br.com.azi.patrimoniointangivel.domain.usecase.fornecedor.buscarlistagem.converter;

import br.com.azi.patrimoniointangivel.domain.entity.Fornecedor;
import br.com.azi.patrimoniointangivel.domain.entity.ListaPaginada;
import br.com.azi.patrimoniointangivel.domain.usecase.fornecedor.buscarlistagem.BuscarFornecedoresOutputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;

import java.util.stream.Collectors;

public class BuscarFornecedoresOutputDataConverter {

    public BuscarFornecedoresOutputData to(ListaPaginada<Fornecedor> from) {
        BuscarPatrimoniosOutputDataItemConverter outputDataItemConverter = new BuscarPatrimoniosOutputDataItemConverter();

        return BuscarFornecedoresOutputData
            .builder()
            .totalElements(from.getTotalElements())
            .totalPages(from.getTotalPages())
            .items(from
                .getItems()
                .stream()
                .map(outputDataItemConverter::to)
                .collect(Collectors.toList()))
            .build();
    }

    private static class BuscarPatrimoniosOutputDataItemConverter extends GenericConverter<Fornecedor, BuscarFornecedoresOutputData.Item> {
    }
}
