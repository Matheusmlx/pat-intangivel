package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporpatrimonio;

import br.com.azi.patrimoniointangivel.domain.entity.ListaPaginada;
import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporpatrimonio.converter.BuscarMovimentacoesPorPatrimonioOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class BuscarMovimentacoesPorPatrimonioUseCaseImpl implements BuscarMovimentacoesPorPatrimonioUseCase {

    private PatrimonioDataProvider patrimonioDataProvider;

    private MovimentacaoDataProvider movimentacaoDataProvider;

    private BuscarMovimentacoesPorPatrimonioOutputDataConverter outputDataConverter;
    @Override
    public BuscarMovimentacoesPorPatrimonioOutputData executar(BuscarMovimentacoesPorPatrimonioInputData inputData) {
        validarDadosEntrada(inputData);
        validarPatrimonioExiste(inputData);

        ListaPaginada<Movimentacao> movimentacoesEncontradas = buscar(inputData.getIdPatrimonio(), inputData.getTamanho());
        return outputDataConverter.to(movimentacoesEncontradas);
    }

    private void validarDadosEntrada(BuscarMovimentacoesPorPatrimonioInputData inputData){
        Validator.of(inputData)
            .validate(BuscarMovimentacoesPorPatrimonioInputData::getIdPatrimonio, Objects::nonNull,"Id do Patrimonio é nulo")
            .get();
    }

    private void validarPatrimonioExiste(BuscarMovimentacoesPorPatrimonioInputData inputData){
        if(!patrimonioDataProvider.existe(inputData.getIdPatrimonio())){
            throw new PatrimonioNaoEncontradoException();
        }
    }

    private ListaPaginada<Movimentacao> buscar(Long patrimonioId, Long tamanho){
      tamanho = validarSeTamanhoEhNullo(tamanho);

      return movimentacaoDataProvider.buscarMovimentacoesPorPatrimonio(patrimonioId, tamanho);
    }

    private Long validarSeTamanhoEhNullo(Long tamanho){
        if(Objects.isNull(tamanho)){
            tamanho = Long.MAX_VALUE;
        }
        return tamanho;
    }

}
