package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.listagempatrimonio.relatorioxls.converter;

import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.listagempatrimonio.relatorioxls.GerarRelatorioListagemPatrimonioXLSInputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;

public class GerarRelatorioListagemPatrimonioConverter extends GenericConverter<GerarRelatorioListagemPatrimonioXLSInputData, Patrimonio.Filtro> {

    @Override
    public Patrimonio.Filtro to(GerarRelatorioListagemPatrimonioXLSInputData from) {
        Patrimonio.Filtro target = super.to(from);
        target.setPage(target.getPage() - 1);
        return target;
    }
}
