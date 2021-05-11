package br.com.azi.patrimoniointangivel.gateway.dataprovider.converter;

import br.com.azi.patrimoniointangivel.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.NotaLancamentoContabilEntity;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import br.com.azi.patrimoniointangivel.utils.date.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class NotaLancamentoContabilConverter extends GenericConverter<NotaLancamentoContabilEntity, NotaLancamentoContabil> {

    @Override
    public NotaLancamentoContabilEntity from(NotaLancamentoContabil source) {
        NotaLancamentoContabilEntity target = super.from(source);

        if (Objects.nonNull(source.getDataLancamento())) {
            target.setDataLancamento(DateUtils.asDate(source.getDataLancamento()));
        } else {
            target.setDataLancamento(null);
        }

        return target;
    }

    @Override
    public NotaLancamentoContabil to(NotaLancamentoContabilEntity source) {
        NotaLancamentoContabil target =  super.to(source);

        if (Objects.nonNull(source.getDataLancamento())) {
            target.setDataLancamento(DateUtils.asLocalDateTime(source.getDataLancamento()));
        }

        return target;
    }
}
