package br.com.azi.patrimoniointangivel.gateway.integration.efornecedor.usecase.buscarporfiltro.converter;

import br.com.azi.patrimoniointangivel.domain.entity.Fornecedor;
import br.com.azi.patrimoniointangivel.domain.entity.ListaPaginada;
import br.com.azi.patrimoniointangivel.gateway.integration.efornecedor.usecase.buscarporfiltro.entity.BuscarFornecedoresPorFiltroIntegrationResponse;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BuscarFornecedoresPorFiltroIntegrationConverter {

    private static final BuscarFornecedoresPorFiltroIntegrationConverter.ItemOutputDataConverter ITEM_OUTPUT_DATA_CONVERTER = new BuscarFornecedoresPorFiltroIntegrationConverter.ItemOutputDataConverter();

    public ListaPaginada<Fornecedor> to(BuscarFornecedoresPorFiltroIntegrationResponse from) {
        return ListaPaginada
            .<Fornecedor>builder()
            .totalElements(from.getTotalElements())
            .totalPages(from.getTotalPages())
            .items(from
                .getContent()
                .stream()
                .map(ITEM_OUTPUT_DATA_CONVERTER::to)
                .collect(Collectors.toList()))
            .build();
    }

    private static class ItemOutputDataConverter extends GenericConverter<BuscarFornecedoresPorFiltroIntegrationResponse.Item, Fornecedor> {
        @Override
        public Fornecedor to(BuscarFornecedoresPorFiltroIntegrationResponse.Item source) {
            Fornecedor target = super.to(source);

            target.setNome(source.getRazaoSocial());

            return target;
        }
    }
}
