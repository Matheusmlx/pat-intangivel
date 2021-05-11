package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarlistagem.converter;

import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarlistagem.BuscarMovimentacoesInputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BuscarMovimentacaoFiltroConverter extends GenericConverter<BuscarMovimentacoesInputData, Movimentacao.Filtro> {

    @Override
    public Movimentacao.Filtro to(BuscarMovimentacoesInputData from) {
        Movimentacao.Filtro target = super.to(from);
        target.setPage(target.getPage() - 1);
        return  target;
    }
}
