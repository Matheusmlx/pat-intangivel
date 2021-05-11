package br.com.azi.patrimoniointangivel.gateway.dataprovider.converter;

import br.com.azi.patrimoniointangivel.domain.entity.ConfigAmortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.ConfigAmortizacaoEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.ContaContabilEntity;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ConfigAmortizacaoConverter extends GenericConverter<ConfigAmortizacaoEntity, ConfigAmortizacao> {

    @Override
    public ConfigAmortizacaoEntity from(ConfigAmortizacao source) {
        ConfigAmortizacaoEntity configAmortizacaoEntity = super.from(source);
        if (Objects.nonNull(source.getContaContabil())) {
            ContaContabilEntity contabilEntity = new ContaContabilEntity();
            contabilEntity.setId(source.getContaContabil().getId());
            configAmortizacaoEntity.setContaContabil(contabilEntity);
        }
        if (Objects.nonNull(source.getMetodo())) {
            configAmortizacaoEntity.setMetodo(source.getMetodo().toString());
        }

        return configAmortizacaoEntity;
    }

    @Override
    public ConfigAmortizacao to(ConfigAmortizacaoEntity configAmortizacaoEntity) {
        ConfigAmortizacao configAmortizacao = super.to(configAmortizacaoEntity);
        configAmortizacao.setContaContabil(contaContabilJpaEntitytoUnidadeOrganizacionalBusinessEntity(configAmortizacaoEntity.getContaContabil()));
        return configAmortizacao;
    }


    private ContaContabil contaContabilJpaEntitytoUnidadeOrganizacionalBusinessEntity(ContaContabilEntity jpa) {
        if (Objects.nonNull(jpa)) {
            return ContaContabil.builder()
                .id(jpa.getId())
                .descricao(jpa.getDescricao())
                .codigo(jpa.getCodigo())
                .build();
        }
        return null;
    }
}
