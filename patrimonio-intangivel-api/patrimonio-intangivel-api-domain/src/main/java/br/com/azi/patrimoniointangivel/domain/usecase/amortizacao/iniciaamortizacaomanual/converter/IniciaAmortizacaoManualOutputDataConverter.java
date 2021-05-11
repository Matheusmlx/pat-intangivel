package br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.iniciaamortizacaomanual.converter;

import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.iniciaamortizacaomanual.IniciaAmortizacaoManualOutputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;

import java.util.List;
import java.util.stream.Collectors;

public class IniciaAmortizacaoManualOutputDataConverter {

    private static final IniciaAmortizacaoManualOutputDataConverter.ItemOutputDataConverter ITEM_OUTPUT_DATA_CONVERTER = new IniciaAmortizacaoManualOutputDataConverter.ItemOutputDataConverter();

    public IniciaAmortizacaoManualOutputData to(List<Patrimonio> source) {
        List<IniciaAmortizacaoManualOutputData.Patrimonio> itens = source.stream()
            .map(this::converterItem)
            .collect(Collectors.toList());

        IniciaAmortizacaoManualOutputData target = new IniciaAmortizacaoManualOutputData();
        target.setItems(itens);

        return target;
    }

    private IniciaAmortizacaoManualOutputData.Patrimonio converterItem(Patrimonio source) {
        return ITEM_OUTPUT_DATA_CONVERTER.to(source);
    }

    private static class ItemOutputDataConverter extends GenericConverter<Patrimonio, IniciaAmortizacaoManualOutputData.Patrimonio> {
    }
}
