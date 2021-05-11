package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscar.converter;

import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.ListaPaginada;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscar.BuscarContasContabeisOutputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;

import java.util.Objects;
import java.util.stream.Collectors;

public class BuscarContasContabeisOutputDataConverter {

    public BuscarContasContabeisOutputData to(ListaPaginada<ContaContabil> from) {
        BuscarPatrimoniosOutputDataItemConverter outputDataItemConverter = new BuscarPatrimoniosOutputDataItemConverter();

        return BuscarContasContabeisOutputData
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

    private static class BuscarPatrimoniosOutputDataItemConverter extends GenericConverter<ContaContabil, BuscarContasContabeisOutputData.Item> {

        @Override
        public BuscarContasContabeisOutputData.Item to(ContaContabil source) {
            BuscarContasContabeisOutputData.Item target = super.to(source);

            target.setNome(source.getDescricao());
            if (Objects.nonNull(source.getConfigContaContabil()) && Objects.nonNull(source.getConfigContaContabil().getTipo())) {
                target.setTipo(source.getConfigContaContabil().getTipo().name());
            }
            if (Objects.nonNull(source.getConfigContaContabil()) && Objects.nonNull(source.getConfigContaContabil().getMetodo())) {
                target.setMetodo(source.getConfigContaContabil().getMetodo().name());
            }
            if(Objects.nonNull(source.getConfigContaContabil()) && Objects.nonNull(source.getConfigContaContabil().getId())){
                target.setIdConfigAmortizacao(source.getConfigContaContabil().getId());
            }


            return target;
        }
    }
}
