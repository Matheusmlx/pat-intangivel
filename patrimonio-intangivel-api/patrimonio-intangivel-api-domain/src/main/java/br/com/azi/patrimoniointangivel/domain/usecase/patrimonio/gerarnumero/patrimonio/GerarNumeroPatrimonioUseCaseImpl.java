package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.patrimonio;

import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.patrimonio.converter.GerarNumeroPatrimonioOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.patrimonio.exception.GerarNumeroPatrimonioException;
import br.com.azi.patrimoniointangivel.utils.string.StringUtils;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class GerarNumeroPatrimonioUseCaseImpl implements GerarNumeroPatrimonioUseCase {

    private PatrimonioDataProvider patrimonioDataProvider;

    private String quantidadeDigitos;

    private GerarNumeroPatrimonioOutputDataConverter outputDataConverter;

    @Override
    public GerarNumeroPatrimonioOutputData executar() {
        verificaQuantidadeDeDigitos();
        String ultimoNumero = buscarUltimoNumero();
        return outputDataConverter.to(geraNumero(ultimoNumero));
    }

    private void verificaQuantidadeDeDigitos() {
        if (Integer.parseInt(quantidadeDigitos) < 8 || Integer.parseInt(quantidadeDigitos) > 15) {
            throw new GerarNumeroPatrimonioException("O número de patrimônio deve ter entre 8 e 15 digitos!");
        }
    }

    private String buscarUltimoNumero() {
        Optional<Patrimonio> patrimonio = patrimonioDataProvider.buscarUltimoAtivado();
        return patrimonio.map(Patrimonio::getNumero).orElse(null);
    }

    private String geraNumero(String numero) {

        if (Objects.nonNull(numero)) {
            int proximonumero = Integer.parseInt(numero) + 1;
            return preencherComZeros(Integer.toString(proximonumero), quantidadeDigitos);
        } else {
            return preencherComZeros("1", quantidadeDigitos);
        }
    }

    private String preencherComZeros(String string, String tamanho) {
        return  StringUtils.padLeftZeros(string, Integer.parseInt(tamanho));
    }

}
