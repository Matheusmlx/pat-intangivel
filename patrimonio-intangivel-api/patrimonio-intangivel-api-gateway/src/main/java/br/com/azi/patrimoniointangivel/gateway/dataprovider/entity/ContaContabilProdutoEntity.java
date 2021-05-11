package br.com.azi.patrimoniointangivel.gateway.dataprovider.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@IdClass(ContaContabilProdutoEntityPK.class)
@Entity
@Table(name = "TB_CONTA_CONTABIL_PRODUTO", schema = "COMUM_SIGA")
public class ContaContabilProdutoEntity {

    @Id
    @Column(name = "CC_ID", insertable = false, updatable = false)
    private Long contaContabilId;

    @Id
    @Column(name = "PR_ID", insertable = false, updatable = false)
    private Long produtoId;
}
