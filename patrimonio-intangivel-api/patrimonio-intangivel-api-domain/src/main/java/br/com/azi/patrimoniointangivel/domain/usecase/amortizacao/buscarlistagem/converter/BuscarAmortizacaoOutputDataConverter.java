package br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.buscarlistagem.converter;

import br.com.azi.patrimoniointangivel.domain.entity.Amortizacao;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.buscarlistagem.BuscarAmortizacaoOutputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BuscarAmortizacaoOutputDataConverter {

    private static final BuscarAmortizacaoOutputDataConverter.ItemOutputDataConverter ITEM_OUTPUT_DATA_CONVERTER = new BuscarAmortizacaoOutputDataConverter.ItemOutputDataConverter();

    public BuscarAmortizacaoOutputData to(List<Amortizacao> source) {
        List<BuscarAmortizacaoOutputData.Amortizacao> itens = source.stream()
            .map(this::converterItem)
            .collect(Collectors.toList());

        BuscarAmortizacaoOutputData target = new BuscarAmortizacaoOutputData();
        target.setItems(itens);

        return target;
    }

    private BuscarAmortizacaoOutputData.Amortizacao converterItem(Amortizacao source) {
        BuscarAmortizacaoOutputData.Amortizacao item = ITEM_OUTPUT_DATA_CONVERTER.to(source);

        if (Objects.nonNull(source.getOrgao())) {
            item.setOrgaoSigla(source.getOrgao().getSigla());
        }

        return item;
    }

    private static class ItemOutputDataConverter extends GenericConverter<Amortizacao, BuscarAmortizacaoOutputData.Amortizacao> {
    }
}

