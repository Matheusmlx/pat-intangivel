package br.com.azi.patrimoniointangivel.gateway.dataprovider.converter;

import br.com.azi.patrimoniointangivel.domain.entity.RelatorioSintetico;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.RelatorioSinteticoEntity;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RelatorioSinteticoConverter  extends GenericConverter<RelatorioSinteticoEntity, RelatorioSintetico> {

    @Autowired
    private ContaContabilConverter contaContabilConverter;

    @Autowired
    private UnidadeOrganizacionalConverter unidadeOrganizacionalConverter;


    @Override
    public RelatorioSintetico to(RelatorioSinteticoEntity source) {
        RelatorioSintetico target = super.to(source);

        if (Objects.nonNull(source.getContaContabil())) {
            target.setContaContabil(contaContabilConverter.to(source.getContaContabil()));
        }
        if(Objects.nonNull(source.getOrgao())){
            target.setOrgao(unidadeOrganizacionalConverter.to(source.getOrgao()));
        }
        if(Objects.nonNull(source.getOrgaoAmortizacao())){
            target.setOrgaoAmortizacao(unidadeOrganizacionalConverter.to(source.getOrgaoAmortizacao()));
        }

        return target;
    }
}
