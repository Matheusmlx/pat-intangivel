package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.edicao.conveter;

import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.edicao.EditarMovimentacaoOutputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import br.com.azi.patrimoniointangivel.utils.validate.DateValidate;

import java.util.Objects;

public class EditarMovimentacaoOutputDataConverter extends GenericConverter<Movimentacao, EditarMovimentacaoOutputData> {

    @Override
    public EditarMovimentacaoOutputData to(Movimentacao source){
        EditarMovimentacaoOutputData target = super.to(source);

        if(Objects.nonNull(source.getOrgaoDestino())){
            target.setOrgaoDestino(source.getOrgaoDestino().getId());
        }

        if(Objects.nonNull(source.getNotaLancamentoContabil())){
            target.setNumeroNL(source.getNotaLancamentoContabil().getNumero());
            if(Objects.nonNull(source.getNotaLancamentoContabil().getDataLancamento())){
                target.setDataNL(DateValidate.formatarData(source.getNotaLancamentoContabil().getDataLancamento()));
            }
        }

        return target;
    }
}
