package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.cadastro.converter;

import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.cadastro.CriarMovimentacaoOutputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;

import java.util.Objects;

public class CriarMovimentacaoOutputDataConverter extends GenericConverter<Movimentacao, CriarMovimentacaoOutputData> {
    @Override
    public CriarMovimentacaoOutputData to(Movimentacao source){
        CriarMovimentacaoOutputData target = super.to(source);

        if(Objects.nonNull(source.getOrgaoOrigem())){
            target.setOrgaoOrigem(source.getOrgaoOrigem().getId());
        }
        return target;
    }
}
