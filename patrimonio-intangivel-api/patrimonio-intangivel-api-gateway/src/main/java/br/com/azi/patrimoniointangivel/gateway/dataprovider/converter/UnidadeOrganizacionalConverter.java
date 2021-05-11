package br.com.azi.patrimoniointangivel.gateway.dataprovider.converter;

import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.UnidadeOrganizacionalEntity;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import org.springframework.stereotype.Component;

@Component
public class UnidadeOrganizacionalConverter extends GenericConverter<UnidadeOrganizacionalEntity, UnidadeOrganizacional> {

    @Override
    public UnidadeOrganizacional to(UnidadeOrganizacionalEntity source) {
        UnidadeOrganizacional target = super.to(source);
        target.setDescricao(String.format("%s - %s", source.getSigla(), source.getNome()));

        return target;
    }
}
