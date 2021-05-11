package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscarporid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarContaContabilPorIdOutputData {
    private Long id;
    private String codigo;
    private String nome;
    private String tipo;
    private String metodo;
    private Short vidaUtil;
    private BigDecimal percentualResidual;
    private String sistema;
}
