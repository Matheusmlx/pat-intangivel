package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarPatrimonioPorMovimentacao;

import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarPatrimonioPorMovimentacao.converter.BuscarPatrimonioPorMovimentacaoOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class BuscarPatrimonioPorMovimentacaoMovimentacaoUseImpl implements BuscarPatrimonioPorMovimentacaoUseCase {

    private PatrimonioDataProvider patrimonioDataProvider;

    private BuscarPatrimonioPorMovimentacaoOutputDataConverter outputDataConverter;

    @Override
    public BuscarPatrimonioPorMovimentacaoOutputData executar(BuscarPatrimonioPorMovimentacaoInputData inputData) {
        validarEntrada(inputData);
        Patrimonio patrimonio = buscarPatrimonio(inputData);

        return outputDataConverter.to(patrimonio);
    }

    private void validarEntrada(BuscarPatrimonioPorMovimentacaoInputData entrada) {
        Validator.of(entrada)
            .validate(BuscarPatrimonioPorMovimentacaoInputData::getId, Objects::nonNull, "Id é nulo")
            .get();
    }

    private Patrimonio buscarPatrimonio(BuscarPatrimonioPorMovimentacaoInputData inputData) {
        Optional<Patrimonio> patrimonio = patrimonioDataProvider.buscarPorId(inputData.getId());
        return (patrimonio.orElseThrow(PatrimonioNaoEncontradoException::new));
    }
}
