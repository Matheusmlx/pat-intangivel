package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.edicao.converter;

import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.edicao.EditarPatrimonioOutputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import br.com.azi.patrimoniointangivel.utils.date.DateUtils;
import br.com.azi.patrimoniointangivel.utils.validate.DateValidate;

import java.util.Objects;

public class EditarPatrimonioOutputDataConverter extends GenericConverter<Patrimonio, EditarPatrimonioOutputData> {

    @Override
    public EditarPatrimonioOutputData to(Patrimonio source) {
        EditarPatrimonioOutputData target = super.to(source);

        if (Objects.nonNull(source.getFornecedor())) {
            target.setFornecedor(source.getFornecedor().getId());
        }
        if (Objects.nonNull(source.getOrgao())) {
            target.setOrgao(source.getOrgao().getId());
        }
        if (Objects.nonNull(source.getSetor())) {
            target.setSetor(source.getSetor().getId());
        }
        if (Objects.nonNull(source.getContaContabil())) {
            target.setContaContabil(
                EditarPatrimonioOutputData.ContaContabil
                    .builder()
                    .id(source.getContaContabil().getId())
                    .codigo(source.getContaContabil().getCodigo())
                    .descricao(source.getContaContabil().getDescricao())
                    .build()
            );
        }
        if (Objects.nonNull(source.getDataAquisicao())) {
            target.setDataAquisicao(DateValidate.formatarData(source.getDataAquisicao()));
        }

        if(Objects.nonNull(source.getNotaLancamentoContabil())){
            target.setNumeroNL(source.getNotaLancamentoContabil().getNumero());
            if(Objects.nonNull(source.getNotaLancamentoContabil().getDataLancamento())){
                target.setDataNL(DateValidate.formatarData(source.getNotaLancamentoContabil().getDataLancamento()));
            }
        }

        if (Objects.nonNull(source.getDataVencimento())) {
            target.setDataVencimento(DateValidate.formatarData(source.getDataVencimento()));
        }
        if (Objects.nonNull(source.getDataAtivacao())) {
            target.setDataAtivacao(DateValidate.formatarData(source.getDataAtivacao()));
        }
        return target;
    }
}
