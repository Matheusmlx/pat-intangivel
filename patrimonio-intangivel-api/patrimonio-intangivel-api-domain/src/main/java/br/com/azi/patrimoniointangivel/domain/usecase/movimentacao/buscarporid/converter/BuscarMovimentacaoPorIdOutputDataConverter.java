package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporid.converter;

import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporid.BuscarMovimentacaoPorIdOutputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import br.com.azi.patrimoniointangivel.utils.validate.DateValidate;

import java.util.Objects;

public class BuscarMovimentacaoPorIdOutputDataConverter extends GenericConverter<Movimentacao, BuscarMovimentacaoPorIdOutputData> {

    @Override
    public BuscarMovimentacaoPorIdOutputData to(Movimentacao source) {
        BuscarMovimentacaoPorIdOutputData target = super.to(source);

       if(Objects.nonNull(source.getDataDeFinalizacao())){
           target.setDataDeFinalizacao(DateValidate.formatarData(source.getDataDeFinalizacao()));
       }

        if(Objects.nonNull(source.getDataDeEnvio())){
            target.setDataDeEnvio(DateValidate.formatarData(source.getDataDeEnvio()));
        }

        if(Objects.nonNull(source.getDataCadastro())){
            target.setDataCadastro(DateValidate.formatarData(source.getDataCadastro()));
        }

        if(Objects.nonNull(source.getNotaLancamentoContabil())){
            target.setNumeroNL(source.getNotaLancamentoContabil().getNumero());
            if(Objects.nonNull(source.getNotaLancamentoContabil().getDataLancamento())){
                target.setDataNL(DateValidate.formatarData(source.getNotaLancamentoContabil().getDataLancamento()));
            }
        }

       if(Objects.nonNull(source.getOrgaoOrigem())){
           target.setOrgaoOrigem(BuscarMovimentacaoPorIdOutputData.UnidadeOrganizacional
               .builder()
               .id(source.getOrgaoOrigem().getId())
               .sigla(source.getOrgaoOrigem().getSigla())
               .descricao(source.getOrgaoOrigem().getDescricao())
               .build());
       }

        if(Objects.nonNull(source.getOrgaoOrigem())){
            target.setOrgaoDestino(BuscarMovimentacaoPorIdOutputData.UnidadeOrganizacional
                .builder()
                .id(source.getOrgaoDestino().getId())
                .sigla(source.getOrgaoDestino().getSigla())
                .descricao(source.getOrgaoDestino().getDescricao())
                .build());
        }

        if(Objects.nonNull(source.getPatrimonio())){
            target.setPatrimonio(source.getPatrimonio().getId());
        }

        return target;
    }
}
