package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarlistagem.converter;

import br.com.azi.patrimoniointangivel.domain.entity.ListaPaginada;
import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarlistagem.BuscarMovimentacoesOutputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import br.com.azi.patrimoniointangivel.utils.validate.DateValidate;

import java.util.Objects;
import java.util.stream.Collectors;

public class BuscarMovimentacoesOutputDataConverter {
    public BuscarMovimentacoesOutputData to(ListaPaginada<Movimentacao> from){
        BuscarMovimentacoesOutputDataItemConverter outputDataItemConverter = new BuscarMovimentacoesOutputDataItemConverter();

        return BuscarMovimentacoesOutputData
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

    private static class BuscarMovimentacoesOutputDataItemConverter extends GenericConverter<Movimentacao, BuscarMovimentacoesOutputData.Item>{
        @Override
        public BuscarMovimentacoesOutputData.Item to(Movimentacao source) {
            BuscarMovimentacoesOutputData.Item target = super.to(source);

            if(Objects.nonNull(source.getOrgaoOrigem())){
                target.setOrgaoOrigem(source.getOrgaoOrigem().getSigla());
            }
            if(Objects.nonNull(source.getOrgaoDestino())){
                target.setOrgaoDestino(source.getOrgaoDestino().getSigla());
            }

            if(Objects.nonNull(source.getDataCadastro())){
                target.setDataCadastro(DateValidate.formatarData(source.getDataCadastro()));
            }

            if (Objects.nonNull(source.getPatrimonio())) {
                target.setPatrimonio(source.getPatrimonio().getId());
            }

            return  target;
        }
    }

}
