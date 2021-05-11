package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscar.converter;

import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscar.BuscarContasContabeisInputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BuscarContasContabeisFiltroConverter extends GenericConverter<BuscarContasContabeisInputData, ContaContabil.Filtro> {

    @Override
    public ContaContabil.Filtro to(BuscarContasContabeisInputData source) {
        ContaContabil.Filtro target = this.superTo(source);
        target.setPage(target.getPage() - 1);
        return target;
    }

    ContaContabil.Filtro superTo(BuscarContasContabeisInputData source) {
        return super.to(source);
    }
}
