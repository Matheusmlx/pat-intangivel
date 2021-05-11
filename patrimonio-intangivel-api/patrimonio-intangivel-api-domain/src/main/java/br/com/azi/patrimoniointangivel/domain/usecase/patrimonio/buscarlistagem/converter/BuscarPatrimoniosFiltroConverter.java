package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarlistagem.converter;

import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarlistagem.BuscarPatrimoniosInputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BuscarPatrimoniosFiltroConverter extends GenericConverter<BuscarPatrimoniosInputData, Patrimonio.Filtro> {

    @Override
    public Patrimonio.Filtro to(BuscarPatrimoniosInputData from) {
        Patrimonio.Filtro target = super.to(from);
        target.setPage(target.getPage() - 1);
        return target;
    }
}
