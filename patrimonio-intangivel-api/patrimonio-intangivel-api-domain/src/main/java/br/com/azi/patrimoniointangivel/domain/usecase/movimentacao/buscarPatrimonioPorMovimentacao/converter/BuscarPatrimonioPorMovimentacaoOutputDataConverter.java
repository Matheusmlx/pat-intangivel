package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarPatrimonioPorMovimentacao.converter;

import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarPatrimonioPorMovimentacao.BuscarPatrimonioPorMovimentacaoOutputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;

import java.util.Objects;

public class BuscarPatrimonioPorMovimentacaoOutputDataConverter extends GenericConverter<Patrimonio, BuscarPatrimonioPorMovimentacaoOutputData> {

    @Override
    public BuscarPatrimonioPorMovimentacaoOutputData to(Patrimonio source) {
        BuscarPatrimonioPorMovimentacaoOutputData target = super.to(source);

        if (Objects.nonNull(source.getSetor())) {
            target.setSetor(source.getSetor().getDescricao());
        }

        return target;
    }
}
