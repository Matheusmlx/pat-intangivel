package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.memorando;

import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.memorando.converter.GerarNumeroMemorandoOutputDataConverter;
import br.com.azi.patrimoniointangivel.utils.string.StringUtils;
import lombok.AllArgsConstructor;

import java.util.Calendar;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class GerarNumeroMemorandoUseCaseImpl implements GerarNumeroMemorandoUseCase {

    private PatrimonioDataProvider patrimonioDataProvider;

    private GerarNumeroMemorandoOutputDataConverter outputDataConverter;

    @Override
    public GerarNumeroMemorandoOutputData executar() {
        return outputDataConverter.to(verificaPatrimonio());
    }

    private Optional<Patrimonio> buscarUltimoNumeroMemorando() {
        return patrimonioDataProvider.buscarUltimoPorMemorando();
    }

    private String verificaPatrimonio() {
        Optional<Patrimonio> patrimonio = buscarUltimoNumeroMemorando();

        if (patrimonio.isPresent()) {
          return  geraNumeroMemorando(patrimonio.get());
        } else {
           return geraPrimeiroNumero();
        }
    }

    private String geraNumeroMemorando(Patrimonio patrimonio) {
        if (Objects.nonNull(patrimonio.getNumeroMemorando()) && Objects.nonNull(patrimonio.getAnoMemorando()) && patrimonio.getAnoMemorando().equals(Calendar.getInstance().get(Calendar.YEAR))) {
            int proximonumero = Integer.parseInt(patrimonio.getNumeroMemorando()) + 1;
            return paddingLeft(Integer.toString(proximonumero), "6");
        }
        return geraPrimeiroNumero();
    }

    private String geraPrimeiroNumero() {
        return paddingLeft("1", "6");
    }

    private String paddingLeft(String string, String tamanho) {
        return StringUtils.padLeftZeros(string, Integer.parseInt(tamanho));
    }

}
