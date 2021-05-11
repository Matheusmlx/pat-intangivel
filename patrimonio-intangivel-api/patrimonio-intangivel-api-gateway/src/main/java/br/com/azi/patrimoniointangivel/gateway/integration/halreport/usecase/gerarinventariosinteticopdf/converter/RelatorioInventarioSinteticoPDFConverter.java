package br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarinventariosinteticopdf.converter;

import br.com.azi.patrimoniointangivel.domain.entity.RelatorioSintetico;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarinventariosinteticopdf.entity.RelatorioInventarioSintetico;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;

public class RelatorioInventarioSinteticoPDFConverter extends GenericConverter<RelatorioSintetico, RelatorioInventarioSintetico> {
    @Override
    public RelatorioInventarioSintetico to(RelatorioSintetico source) {
        return super.to(source);
    }
}
