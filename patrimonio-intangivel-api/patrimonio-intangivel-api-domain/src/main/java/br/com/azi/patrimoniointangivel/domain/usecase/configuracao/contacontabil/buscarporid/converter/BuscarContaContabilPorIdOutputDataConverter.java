package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscarporid.converter;

import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscarporid.BuscarContaContabilPorIdOutputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;

import java.util.Objects;

public class BuscarContaContabilPorIdOutputDataConverter extends GenericConverter<ContaContabil, BuscarContaContabilPorIdOutputData> {
    @Override
    public BuscarContaContabilPorIdOutputData to(ContaContabil source) {
        BuscarContaContabilPorIdOutputData target = super.to(source);

        if (Objects.nonNull(source.getConfigContaContabil())) {
            target.setTipo(source.getConfigContaContabil().getTipo().name());
            target.setMetodo(Objects.nonNull(source.getConfigContaContabil().getMetodo()) ? source.getConfigContaContabil().getMetodo().name() : null);
        }

        return target;
    }
}
