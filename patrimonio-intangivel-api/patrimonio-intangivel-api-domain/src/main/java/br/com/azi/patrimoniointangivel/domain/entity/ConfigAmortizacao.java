package br.com.azi.patrimoniointangivel.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfigAmortizacao {

    private Long id;
    private Metodo metodo;
    private Short vidaUtil;
    private Tipo tipo;
    private String sistema;
    private Situacao situacao;
    private BigDecimal taxa;
    private BigDecimal percentualResidual;
    private ContaContabil contaContabil;

    public enum Situacao {
        ATIVO,
        INATIVO
    }

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
