package br.com.azi.patrimoniointangivel.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UnidadeOrganizacional {
    private Long id;
    private String nome;
    private String sigla;
    private String codHierarquia;
    private String situacao;
    private String descricao;
    private TipoAdministracao tipoAdministracao;

    @AllArgsConstructor
    public enum TipoAdministracao {
        DIRETA,
        AUTARQUIA,
        EMPRESA_PUBLICA,
        FUNDACAO,
        ECONOMIA_MISTA,
        FUNDOS,
        REGIME_ESPECIAL
    }
}
