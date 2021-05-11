package br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarinventariosinteticoxls.converter;

import br.com.azi.patrimoniointangivel.domain.entity.RelatorioSintetico;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarinventariosinteticoxls.entity.RelatorioInventarioSintetico;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;

public class RelatorioInventarioSinteticoXLSConverter extends GenericConverter<RelatorioSintetico, RelatorioInventarioSintetico> {
    @Override
    public RelatorioInventarioSintetico to(RelatorioSintetico source) {
        return super.to(source);
    }
}
