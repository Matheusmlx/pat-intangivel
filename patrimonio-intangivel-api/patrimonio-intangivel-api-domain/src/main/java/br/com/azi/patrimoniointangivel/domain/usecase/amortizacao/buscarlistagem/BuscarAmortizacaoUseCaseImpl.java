package br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.buscarlistagem;

import br.com.azi.patrimoniointangivel.domain.entity.Amortizacao;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.AmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.buscarlistagem.converter.BuscarAmortizacaoOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class BuscarAmortizacaoUseCaseImpl implements BuscarAmortizacaoUseCase {

    private AmortizacaoDataProvider amortizacaoDataProvider;
    private BuscarAmortizacaoOutputDataConverter outputDataConverter;

    @Override
    public BuscarAmortizacaoOutputData executar(BuscarAmortizacaoInputData inputData) {
        validarDadosEntrada(inputData);
        List<Amortizacao> entidadesEncontradas = buscar(inputData);

        return outputDataConverter.to(entidadesEncontradas);
    }

    private void validarDadosEntrada(BuscarAmortizacaoInputData entrada) {
        Validator.of(entrada)
            .validate(BuscarAmortizacaoInputData::getId, Objects::nonNull, "O id é nulo")
            .get();
    }

    private List<Amortizacao> buscar(BuscarAmortizacaoInputData inputData) {
        Long patrimonioId = inputData.getId();
        return amortizacaoDataProvider.buscar(patrimonioId);
    }
}
