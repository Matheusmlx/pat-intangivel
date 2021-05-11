package br.com.azi.patrimoniointangivel.gateway.dataprovider.entity;

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
public class LancamentosContabeisAgrupadoEntity {
    PatrimonioEntity patrimonio;
    UnidadeOrganizacionalEntity orgao;
    LocalDateTime maiorData;
    BigDecimal maiorValorLiquido;
}
