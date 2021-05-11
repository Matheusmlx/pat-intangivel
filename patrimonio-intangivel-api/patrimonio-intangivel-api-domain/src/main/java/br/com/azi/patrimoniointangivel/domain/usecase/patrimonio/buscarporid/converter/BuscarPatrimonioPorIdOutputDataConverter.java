package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarporid.converter;

import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarporid.BuscarPatrimonioPorIdOutputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import br.com.azi.patrimoniointangivel.utils.validate.DateValidate;

import java.util.Objects;

public class BuscarPatrimonioPorIdOutputDataConverter extends GenericConverter<Patrimonio, BuscarPatrimonioPorIdOutputData> {

    @Override
    public BuscarPatrimonioPorIdOutputData to(Patrimonio source) {
        BuscarPatrimonioPorIdOutputData target = super.to(source);

        if (Objects.nonNull(source.getOrgao())) {
            target.setOrgao(source.getOrgao().getId());
        }
        if (Objects.nonNull(source.getSetor())) {
            target.setSetor(source.getSetor().getId());
        }
        if (Objects.nonNull(source.getFornecedor())) {
            target.setFornecedor(source.getFornecedor().getId());
        }

        if (Objects.nonNull(source.getDataVencimento())) {
            target.setDataVencimento(DateValidate.formatarData(source.getDataVencimento()));
        }

        if(Objects.nonNull(source.getNotaLancamentoContabil())){
            target.setNumeroNL(source.getNotaLancamentoContabil().getNumero());
            if(Objects.nonNull(source.getNotaLancamentoContabil().getDataLancamento())){
                target.setDataNL(DateValidate.formatarData(source.getNotaLancamentoContabil().getDataLancamento()));
            }
        }


        if (Objects.nonNull(source.getDataAtivacao())) {
            target.setDataAtivacao(DateValidate.formatarData(source.getDataAtivacao()));
        }

        if (Objects.nonNull(source.getDataAquisicao())) {
            target.setDataAquisicao(DateValidate.formatarData(source.getDataAquisicao()));
        }

        if (Objects.nonNull(source.getContaContabil())) {
            target.setContaContabil(
                BuscarPatrimonioPorIdOutputData.ContaContabil
                    .builder()
                    .id(source.getContaContabil().getId())
                    .codigo(source.getContaContabil().getCodigo())
                    .descricao(source.getContaContabil().getDescricao())
                    .build()
            );
        }

        return target;
    }
}
