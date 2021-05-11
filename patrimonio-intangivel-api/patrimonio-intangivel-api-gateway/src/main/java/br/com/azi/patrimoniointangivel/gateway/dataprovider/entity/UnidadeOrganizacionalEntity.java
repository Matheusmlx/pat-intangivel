package br.com.azi.patrimoniointangivel.gateway.dataprovider.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_UNIDADE_ORGANIZACIONAL", schema = "COMUM_SIGA")
public class UnidadeOrganizacionalEntity {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "UO_ID", insertable = false, updatable = false)
    private Long id;

    @Column(name = "UO_NOME", insertable = false, updatable = false)
    private String nome;

    @Column(name = "UO_SIGLA", insertable = false, updatable = false)
    private String sigla;

    @Column(name = "UO_SITUACAO", insertable = false, updatable = false)
    private String situacao;

    @Column(name = "UO_TIPO", insertable = false, updatable = false)
    private String tipo;

    @Column(name = "UO_TIPO_ADM", insertable = false, updatable = false)
    private String tipoAdministracao;

    @Column(name = "UO_COD_HIERARQUIA", insertable = false, updatable = false)
    private String codigoHierarquia;

    @Column(name = "UO_ALMOXARIFADO", insertable = false, updatable = false)
    private Boolean almoxarifado;

    @Column(name = "UO_ID_SUPERIOR", insertable = false, updatable = false)
    private Long unidadeSuperiorId;

    @Column(name = "UO_ESTRUTURA_ADMINISTRATIVA", insertable = false, updatable = false)
    protected Long estruturaAdministrativaId;
}
