package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.gerarcodigo;

import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.gerarcodigo.converter.GerarCodigoDemovimentacaoOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.gerarcodigo.exception.GerarCodigoMovimentacaoException;
import br.com.azi.patrimoniointangivel.utils.string.StringUtils;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class GerarCodigoDeMovimentacaoUseCaseImpl implements GerarCodigoDeMovimentacaoUseCase {

    private MovimentacaoDataProvider movimentacaoDataProvider;

    private String quantidadeDigitos;

    private GerarCodigoDemovimentacaoOutputDataConverter outputDataConverter;

    @Override
    public GerarCodigoDeMovimentacaoOutputData executar() {
        verificarQuantidadeDeDigitos();
        String ultimoNumero = buscarUltimoNumero();

        return outputDataConverter.to(gerarCodigo(ultimoNumero));
    }

    private void verificarQuantidadeDeDigitos(){
        if (Integer.parseInt(quantidadeDigitos) < 5 || Integer.parseInt(quantidadeDigitos) > 10) {
            throw new GerarCodigoMovimentacaoException("O codigo de movimentacao deve ter entre 5 e 10 digitos!");
        }
    }

    private String buscarUltimoNumero(){
        Optional<Movimentacao> movimentacao = movimentacaoDataProvider.buscarUltimoCriado();
        return movimentacao.map(Movimentacao::getCodigo).orElse(null);
    }

    private String gerarCodigo(String codigo){
        if (Objects.nonNull(codigo)) {
            int proximonumero = Integer.parseInt(codigo) + 1;
            return paddingLeft(Integer.toString(proximonumero), quantidadeDigitos);
        } else {
            return paddingLeft("1", quantidadeDigitos);
        }
    }

    private String paddingLeft(String string, String tamanho) {
        return StringUtils.padLeftZeros(string, Integer.parseInt(tamanho));
    }
}
