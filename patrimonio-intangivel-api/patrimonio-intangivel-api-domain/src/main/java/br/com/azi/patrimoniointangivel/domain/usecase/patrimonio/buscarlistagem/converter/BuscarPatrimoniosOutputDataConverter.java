package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarlistagem.converter;

import br.com.azi.patrimoniointangivel.domain.entity.ListaPaginada;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarlistagem.BuscarPatrimoniosOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarporid.BuscarPatrimonioPorIdOutputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import br.com.azi.patrimoniointangivel.utils.validate.DateValidate;

import java.util.Objects;
import java.util.stream.Collectors;

public class BuscarPatrimoniosOutputDataConverter {

    public BuscarPatrimoniosOutputData to(ListaPaginada<Patrimonio> from) {
        BuscarPatrimoniosOutputDataItemConverter outputDataItemConverter = new BuscarPatrimoniosOutputDataItemConverter();

        return BuscarPatrimoniosOutputData
            .builder()
            .totalElements(from.getTotalElements())
            .totalPages(from.getTotalPages())
            .items(from
                .getItems()
                .stream()
                .map(outputDataItemConverter::to)
                .collect(Collectors.toList()))
            .build();
    }

    private static class BuscarPatrimoniosOutputDataItemConverter extends GenericConverter<Patrimonio, BuscarPatrimoniosOutputData.Item> {
        @Override
        public BuscarPatrimoniosOutputData.Item to(Patrimonio source) {
            BuscarPatrimoniosOutputData.Item target = super.to(source);

            if (Objects.nonNull(source.getOrgao())) {
                target.setOrgao(source.getOrgao().getSigla());
            }

            if (Objects.nonNull(source.getSetor())) {
                target.setSetor(source.getSetor().getSigla());
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
                    BuscarPatrimoniosOutputData.Item.ContaContabil
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
}
