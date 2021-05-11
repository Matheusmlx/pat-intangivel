package br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarinventarioanaliticoxls.converter;

import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarinventarioanaliticoxls.entity.RelatorioInventarioAnalitico;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;

public class RelatorioInventarioAnaliticoPatrimonioConverter extends GenericConverter<Patrimonio, RelatorioInventarioAnalitico.Patrimonio> {
    @Override
    public RelatorioInventarioAnalitico.Patrimonio to(Patrimonio source) {
        return super.to(source);
    }
}
