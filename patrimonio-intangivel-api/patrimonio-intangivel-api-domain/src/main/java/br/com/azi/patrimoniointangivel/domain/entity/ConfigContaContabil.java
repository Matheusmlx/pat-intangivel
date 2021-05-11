package br.com.azi.patrimoniointangivel.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfigContaContabil {
    private Long id;
    private Metodo metodo;
    private Tipo tipo;
    private ContaContabil contaContabil;

    public enum Metodo {
        QUOTAS_CONSTANTES,
        SOMA_DOS_DIGITOS
    }

    public enum Tipo {
        DEPRECIAVEL,
        AMORTIZAVEL,
        NAO_AMORTIZAVEL
    }

}
