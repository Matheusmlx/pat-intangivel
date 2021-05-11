package br.com.azi.patrimoniointangivel.gateway.dataprovider.converter;


import br.com.azi.patrimoniointangivel.domain.entity.Amortizacao;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.AmortizacaoEntity;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import br.com.azi.patrimoniointangivel.utils.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AmortizacaoConverter extends GenericConverter<AmortizacaoEntity, Amortizacao> {

    @Autowired
    PatrimonioConverter patrimonioConverter;

    @Autowired
    ConfigAmortizacaoConverter configAmortizacaoConverter;

    @Autowired
    UnidadeOrganizacionalConverter unidadeOrganizacionalConverter;

    @Override
    public AmortizacaoEntity from(Amortizacao source) {
        AmortizacaoEntity target = super.from(source);

        if (Objects.nonNull(source.getDataInicial())) {
            target.setDataInicial(DateUtils.asDate(source.getDataInicial()));
        }

        if (Objects.nonNull(source.getDataFinal())) {
            target.setDataFinal(DateUtils.asDate(source.getDataFinal()));
        }

        if (Objects.nonNull(source.getPatrimonio())) {
            target.setPatrimonio(patrimonioConverter.from(source.getPatrimonio()));
        }

        if (Objects.nonNull(source.getConfigAmortizacao())) {
            target.setConfigAmortizacao(configAmortizacaoConverter.from(source.getConfigAmortizacao()));
        }

        if (Objects.nonNull(source.getOrgao())) {
            target.setOrgao(unidadeOrganizacionalConverter.from(source.getOrgao()));
        }
        return target;
    }

    @Override
    public Amortizacao to(AmortizacaoEntity source) {
        Amortizacao target = super.to(source);

        if (Objects.nonNull(source.getDataInicial())) {
            target.setDataInicial(DateUtils.asLocalDateTime(source.getDataInicial()));
        }

        if (Objects.nonNull(source.getDataFinal())) {
            target.setDataFinal(DateUtils.asLocalDateTime(source.getDataFinal()));
        }

        if (Objects.nonNull(source.getPatrimonio())) {
            target.setPatrimonio(patrimonioConverter.to(source.getPatrimonio()));
        }

        if (Objects.nonNull(source.getConfigAmortizacao())) {
            target.setConfigAmortizacao(configAmortizacaoConverter.to(source.getConfigAmortizacao()));
        }

        if (Objects.nonNull(source.getOrgao())) {
            target.setOrgao(unidadeOrganizacionalConverter.to(source.getOrgao()));
        }

        return target;
    }

}
