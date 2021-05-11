package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporpatrimonio.converter;

import br.com.azi.patrimoniointangivel.domain.entity.ListaPaginada;
import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporpatrimonio.BuscarMovimentacoesPorPatrimonioOutputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;

import java.util.Objects;
import java.util.stream.Collectors;

public class BuscarMovimentacoesPorPatrimonioOutputDataConverter {

    public BuscarMovimentacoesPorPatrimonioOutputData to (ListaPaginada<Movimentacao> from){
        BuscarPorPatrimonioOutputDataItemConverter outputDataItemConverter = new BuscarPorPatrimonioOutputDataItemConverter();

        return BuscarMovimentacoesPorPatrimonioOutputData
            .builder()
            .items(from
                .getItems()
                .stream()
                .map(outputDataItemConverter::to)
                 .collect(Collectors.toList()))
            .build();
    }

    private static class BuscarPorPatrimonioOutputDataItemConverter extends GenericConverter<Movimentacao, BuscarMovimentacoesPorPatrimonioOutputData.Item>{
        @Override
        public BuscarMovimentacoesPorPatrimonioOutputData.Item to(Movimentacao source) {
            BuscarMovimentacoesPorPatrimonioOutputData.Item target = super.to(source);

            if(Objects.nonNull(source.getId())){
                target.setId(source.getId());
            }

            if(Objects.nonNull(source.getOrgaoOrigem())){
                target.setOrgaoOrigem(BuscarMovimentacoesPorPatrimonioOutputData.UnidadeOrganizacional.builder()
                    .id(source.getOrgaoOrigem().getId())
                    .descricao(source.getOrgaoOrigem().getDescricao())
                    .nome(source.getOrgaoOrigem().getNome())
                    .sigla(source.getOrgaoOrigem().getSigla())
                    .build());
            }
            if(Objects.nonNull(source.getOrgaoDestino())){
                target.setOrgaoDestino(BuscarMovimentacoesPorPatrimonioOutputData.UnidadeOrganizacional.builder()
                    .id(source.getOrgaoDestino().getId())
                    .descricao(source.getOrgaoDestino().getDescricao())
                    .nome(source.getOrgaoDestino().getNome())
                    .sigla(source.getOrgaoDestino().getSigla())
                    .build());
            }

            if(Objects.isNull(source.getDataDeEnvio())){
                target.setDataDeAlteracao(source.getDataCadastro());
            }

            if(Objects.nonNull(source.getDataDeEnvio())){
                target.setDataDeAlteracao(source.getDataDeEnvio());
            }

            return target;
        }
    }


}
