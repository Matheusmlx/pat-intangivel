package br.com.azi.patrimoniointangivel.gateway.dataprovider.converter;

import br.com.azi.patrimoniointangivel.domain.entity.LancamentosContabeis;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.LancamentosContabeisEntity;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import br.com.azi.patrimoniointangivel.utils.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class LancamentosContabeisConverter extends GenericConverter<LancamentosContabeisEntity, LancamentosContabeis> {

    @Autowired
    UnidadeOrganizacionalConverter unidadeOrganizacionalConverter;

    @Autowired
    PatrimonioConverter patrimonioConverter;

    @Autowired
    MovimentacaoConverter movimentacaoConverter;

    @Autowired
    ContaContabilConverter contaContabilConverter;

    @Override
    public LancamentosContabeisEntity from(LancamentosContabeis source) {
        LancamentosContabeisEntity target = super.from(source);

        if (Objects.nonNull(source.getPatrimonio())) {
            target.setPatrimonio(patrimonioConverter.from(source.getPatrimonio()));
        }

        if (Objects.nonNull(source.getMovimentacao())) {
            target.setMovimentacao(movimentacaoConverter.from(source.getMovimentacao()));
        }

        if (Objects.nonNull(source.getOrgao())) {
            target.setOrgao(unidadeOrganizacionalConverter.from(source.getOrgao()));
        }

        if (Objects.nonNull(source.getContaContabil())) {
            target.setContaContabil(contaContabilConverter.from(source.getContaContabil()));
        }

        if (Objects.nonNull(source.getDataLancamento())) {
            target.setDataLancamento(source.getDataLancamento());
        }

        return target;
    }

    @Override
    public LancamentosContabeis to(LancamentosContabeisEntity source) {
        LancamentosContabeis target = super.to(source);

        if (Objects.nonNull(source.getPatrimonio())) {
            target.setPatrimonio(patrimonioConverter.to(source.getPatrimonio()));
        }

        if (Objects.nonNull(source.getMovimentacao())) {
            target.setMovimentacao(movimentacaoConverter.to(source.getMovimentacao()));
        }

        if (Objects.nonNull(source.getOrgao())) {
            target.setOrgao(unidadeOrganizacionalConverter.to(source.getOrgao()));
        }

        if (Objects.nonNull(source.getContaContabil())) {
            target.setContaContabil(contaContabilConverter.to(source.getContaContabil()));
        }

        if (Objects.nonNull(source.getDataLancamento())) {
            target.setDataLancamento(DateUtils.asLocalDateTime(source.getDataLancamento()));
        }

        return target;
    }
}
