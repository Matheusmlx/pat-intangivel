package br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.iniciaamortizacaomanual;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IniciaAmortizacaoManualOutputData {

    List<Patrimonio> items;

    @Data
    @NoArgsConstructor
    public static class Patrimonio {
        private Long id;
        private String numero;
    }
}
