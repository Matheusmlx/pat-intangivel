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
@Table(name = "TB_PRODUTO", schema = "COMUM")
public class ProdutoEntity {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "PR_ID", insertable = false, updatable = false)
    private Long id;

}
