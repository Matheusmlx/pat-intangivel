package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.parametro.buscar.converter;

import br.com.azi.patrimoniointangivel.domain.entity.PropriedadesProjeto;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.parametro.buscar.BuscarParametrosOutputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import br.com.azi.patrimoniointangivel.utils.date.DateUtils;
import br.com.azi.patrimoniointangivel.utils.validate.DateValidate;

import java.util.Objects;

public class BuscarParametrosOutputDataConverter extends GenericConverter<PropriedadesProjeto, BuscarParametrosOutputData> {

    @Override
    public BuscarParametrosOutputData to(PropriedadesProjeto source) {
        BuscarParametrosOutputData target = super.to(source);
        if (Objects.nonNull(source.getDataLimiteRetroativo())) {
            target.setDataLimiteRetroativo(DateValidate.formatarData(
                DateUtils.converterStringParaLocalDateTime(source.getDataLimiteRetroativo())
            ));
        }

        if(Objects.nonNull(source.getDataCorteInicioCadastroRetroativo()) && !source.getDataCorteInicioCadastroRetroativo().isEmpty()){
            target.setDataCorteInicioCadastroRetroativo(
                DateValidate.formatarData(
                    DateUtils.converterStringParaLocalDateTime(source.getDataCorteInicioCadastroRetroativo())
                ));
        }

        return target;
    }

}
