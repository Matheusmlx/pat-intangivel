package br.com.azi.patrimoniointangivel.gateway.dataprovider.converter;

import br.com.azi.patrimoniointangivel.domain.entity.ConfigAmortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.DadosAmortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.ConfigAmortizacaoEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.DadosAmortizacaoEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.PatrimonioEntity;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DadosAmortizacaoConverter extends GenericConverter<DadosAmortizacaoEntity, DadosAmortizacao> {

    @Override
    public DadosAmortizacaoEntity from(DadosAmortizacao source) {
        DadosAmortizacaoEntity dadosAmortizacaoEntity = super.from(source);
        if (Objects.nonNull(source.getConfigAmortizacao())) {
            ConfigAmortizacaoEntity configAmortizacaoEntity = new ConfigAmortizacaoEntity();
            configAmortizacaoEntity.setId(source.getConfigAmortizacao().getId());
            configAmortizacaoEntity.setMetodo(source.getConfigAmortizacao().getMetodo().name());
            configAmortizacaoEntity.setVidaUtil(source.getConfigAmortizacao().getVidaUtil());
            configAmortizacaoEntity.setSituacao(source.getConfigAmortizacao().getSituacao().name());
            configAmortizacaoEntity.setTaxa(source.getConfigAmortizacao().getTaxa());
            configAmortizacaoEntity.setPercentualResidual(source.getConfigAmortizacao().getPercentualResidual());
            dadosAmortizacaoEntity.setConfigAmortizacao(configAmortizacaoEntity);
        }
        if (Objects.nonNull(source.getPatrimonio())) {
            PatrimonioEntity patrimonioEntity = new PatrimonioEntity();
            patrimonioEntity.setId(source.getPatrimonio().getId());
            dadosAmortizacaoEntity.setPatrimonio(patrimonioEntity.getId());
        }
        return dadosAmortizacaoEntity;
    }

    @Override
    public DadosAmortizacao to(DadosAmortizacaoEntity dadosAmortizacaoEntity) {
        DadosAmortizacao dadosAmortizacao = super.to(dadosAmortizacaoEntity);
        dadosAmortizacao.setConfigAmortizacao(dadosAmortizacaoJpaEntitytoDadosAmortizacaoBusinessEntity(dadosAmortizacaoEntity.getConfigAmortizacao()));
        dadosAmortizacao.setPatrimonio(patrimonioJpaEntitytoPatrimonioBusinessEntity(dadosAmortizacaoEntity.getPatrimonio()));
        return dadosAmortizacao;
    }


    private ConfigAmortizacao dadosAmortizacaoJpaEntitytoDadosAmortizacaoBusinessEntity(ConfigAmortizacaoEntity jpa) {
        if (Objects.nonNull(jpa) && Objects.nonNull(jpa.getMetodo())) {
            return ConfigAmortizacao.builder()
                .id(jpa.getId())
                .metodo(ConfigAmortizacao.Metodo.valueOf(jpa.getMetodo()))
                .vidaUtil(jpa.getVidaUtil())
                .situacao(ConfigAmortizacao.Situacao.valueOf(jpa.getSituacao()))
                .taxa(jpa.getTaxa())
                .percentualResidual(jpa.getPercentualResidual())
                .build();
        }else if(Objects.nonNull(jpa)){
                return ConfigAmortizacao.builder()
                    .id(jpa.getId())
                    .vidaUtil(jpa.getVidaUtil())
                    .situacao(ConfigAmortizacao.Situacao.valueOf(jpa.getSituacao()))
                    .taxa(jpa.getTaxa())
                    .percentualResidual(jpa.getPercentualResidual())
                    .build();
        }
        return null;
    }

    private Patrimonio patrimonioJpaEntitytoPatrimonioBusinessEntity(Long patrimonio) {
        if (Objects.nonNull(patrimonio)) {
            return Patrimonio.builder()
                .id(patrimonio)
                .build();
        }
        return null;
    }


}
