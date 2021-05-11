package br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarinventarioanaliticopdf.converter;

import br.com.azi.patrimoniointangivel.domain.entity.RelatorioAnalitico;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarinventarioanaliticopdf.entity.RelatorioInventarioAnalitico;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;

public class RelatorioInventarioAnaliticoPDFConverter extends GenericConverter<RelatorioAnalitico, RelatorioInventarioAnalitico> {
    @Override
    public RelatorioInventarioAnalitico to(RelatorioAnalitico source){return super.to(source);}
}
