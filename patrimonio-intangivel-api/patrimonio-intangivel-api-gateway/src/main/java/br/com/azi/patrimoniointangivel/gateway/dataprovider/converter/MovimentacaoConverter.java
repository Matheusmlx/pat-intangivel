package br.com.azi.patrimoniointangivel.gateway.dataprovider.converter;

import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.MovimentacaoEntity;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import br.com.azi.patrimoniointangivel.utils.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class MovimentacaoConverter extends GenericConverter<MovimentacaoEntity, Movimentacao> {

    @Autowired
    UnidadeOrganizacionalConverter unidadeOrganizacionalConverter;

    @Autowired
    PatrimonioConverter patrimonioConverter;

    @Autowired
    private NotaLancamentoContabilConverter notaLancamentoContabilConverter;

    @Override
    public MovimentacaoEntity from(Movimentacao source) {
        MovimentacaoEntity target = super.from(source);

      if(Objects.nonNull(source.getOrgaoOrigem())){
          target.setOrgaoOrigem(unidadeOrganizacionalConverter.from(source.getOrgaoOrigem()));
      }

      if(Objects.nonNull(source.getOrgaoDestino())){
          target.setOrgaoDestino(unidadeOrganizacionalConverter.from(source.getOrgaoDestino()));
      }

      if(Objects.nonNull(source.getPatrimonio())){
          target.setPatrimonio(patrimonioConverter.from(source.getPatrimonio()));
      }

      if(Objects.nonNull(source.getDataDeEnvio())) {
          target.setDataDeEnvio(DateUtils.asDate(source.getDataDeEnvio()));
      }

      if(Objects.nonNull(source.getDataDeFinalizacao())) {
          target.setDataDeFinalizacao(DateUtils.asDate(source.getDataDeFinalizacao()));
      }

      if (Objects.nonNull(source.getDataCadastro())) {
          target.setDataCadastro(DateUtils.asDate(source.getDataCadastro()));
      }

        if(Objects.nonNull(source.getNotaLancamentoContabil())){
            target.setNotaLancamentoContabil(notaLancamentoContabilConverter.from(source.getNotaLancamentoContabil()));
        }
      return target;
    }

    @Override
    public Movimentacao to(MovimentacaoEntity source) {
        Movimentacao target = super.to(source);

        if(Objects.nonNull(source.getOrgaoOrigem())){
            target.setOrgaoOrigem(unidadeOrganizacionalConverter.to(source.getOrgaoOrigem()));
        }

        if(Objects.nonNull(source.getOrgaoDestino())){
            target.setOrgaoDestino(unidadeOrganizacionalConverter.to(source.getOrgaoDestino()));
        }

        if(Objects.nonNull(source.getPatrimonio())){
            target.setPatrimonio(patrimonioConverter.to(source.getPatrimonio()));
        }

        if (Objects.nonNull(source.getDataDeEnvio())) {
            target.setDataDeEnvio(DateUtils.asLocalDateTime(source.getDataDeEnvio()));
        }

        if(Objects.nonNull(source.getDataDeFinalizacao())) {
            target.setDataDeFinalizacao(DateUtils.asLocalDateTime(source.getDataDeFinalizacao()));
        }

        if (Objects.nonNull(source.getDataCadastro())) {
            target.setDataCadastro(DateUtils.asLocalDateTime(source.getDataCadastro()));
        }

        if (Objects.nonNull(source.getNotaLancamentoContabil())) {
            target.setNotaLancamentoContabil(notaLancamentoContabilConverter.to(source.getNotaLancamentoContabil()));
        }

        return target;
    }

}
