package br.com.azi.patrimoniointangivel.gateway.dataprovider.converter;

import br.com.azi.patrimoniointangivel.domain.entity.PatrimoniosAgrupados;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.PatrimoniosAgrupadosEntity;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatrimoniosAgrupadosConverter extends GenericConverter<PatrimoniosAgrupadosEntity, PatrimoniosAgrupados> {

    @Autowired
    UnidadeOrganizacionalConverter unidadeOrganizacionalConverter;

    @Autowired
    PatrimonioConverter patrimonioConverter;

    @Override
    public PatrimoniosAgrupadosEntity from(PatrimoniosAgrupados source) {
        PatrimoniosAgrupadosEntity target = super.from(source);

        return target;
    }

    @Override
    public PatrimoniosAgrupados to(PatrimoniosAgrupadosEntity source) {
        PatrimoniosAgrupados target = super.to(source);

        return target;
    }
}
