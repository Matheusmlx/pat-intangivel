package br.com.azi.patrimoniointangivel.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LancamentosContabeisAgrupado {
    Patrimonio patrimonio;
    UnidadeOrganizacional orgao;
    LocalDateTime maiorData;
    BigDecimal maiorValorLiquido;
}
