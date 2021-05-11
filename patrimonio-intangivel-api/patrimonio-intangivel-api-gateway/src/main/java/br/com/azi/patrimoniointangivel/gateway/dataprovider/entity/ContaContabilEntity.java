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

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_CONTA_CONTABIL", schema = "COMUM_SIGA")
public class ContaContabilEntity {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "CC_ID", insertable = false, updatable = false)
    private Long id;

    @Column(name = "CC_CODIGO", insertable = false, updatable = false)
    private String codigo;

    @Column(name = "CC_DESCRICAO", insertable = false, updatable = false)
    private String descricao;

    @Column(name = "CC_SITUACAO", insertable = false, updatable = false)
    private String situacao;
}
