package br.com.azi.patrimoniointangivel.gateway.dataprovider.converter;

import br.com.azi.patrimoniointangivel.domain.entity.LancamentosContabeisAgrupado;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.LancamentosContabeisAgrupadoEntity;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class LancamentosContabeisAgrupadoConverter extends GenericConverter<LancamentosContabeisAgrupadoEntity, LancamentosContabeisAgrupado> {

    @Autowired
    UnidadeOrganizacionalConverter unidadeOrganizacionalConverter;

    @Autowired
    PatrimonioConverter patrimonioConverter;

    @Override
    public LancamentosContabeisAgrupadoEntity from(LancamentosContabeisAgrupado source) {
        LancamentosContabeisAgrupadoEntity target = super.from(source);

        if (Objects.nonNull(source.getPatrimonio())) {
            target.setPatrimonio(patrimonioConverter.from(source.getPatrimonio()));
        }

        if (Objects.nonNull(source.getOrgao())) {
            target.setOrgao(unidadeOrganizacionalConverter.from(source.getOrgao()));
        }

        return target;
    }

    @Override
    public LancamentosContabeisAgrupado to(LancamentosContabeisAgrupadoEntity source) {
        LancamentosContabeisAgrupado target = super.to(source);

        if (Objects.nonNull(source.getPatrimonio())) {
            target.setPatrimonio(patrimonioConverter.to(source.getPatrimonio()));
        }

        if (Objects.nonNull(source.getOrgao())) {
            target.setOrgao(unidadeOrganizacionalConverter.to(source.getOrgao()));
        }

        return target;
    }
}
