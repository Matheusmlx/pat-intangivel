package br.com.azi.patrimoniointangivel.gateway.integration.hal.usecase.unidadeorganizacional.verificardominiounidadeorganizacionalporidefuncao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificarDominioUnidadeOrganizacionalPorIdEFuncaoIntegrationResponse {

    private Boolean possuiDominio;
}
