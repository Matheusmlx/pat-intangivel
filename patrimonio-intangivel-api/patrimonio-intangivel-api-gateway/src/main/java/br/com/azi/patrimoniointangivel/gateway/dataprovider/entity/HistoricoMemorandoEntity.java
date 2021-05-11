package br.com.azi.patrimoniointangivel.gateway.dataprovider.entity;

import br.com.azi.hal.core.generic.entity.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Data
@Builder
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_HISTORICO_MEMORANDO", schema = "PAT_INTANGIVEL")
@SequenceGenerator(name = "SEQ_HISTORICO_MEMORANDO", sequenceName = "PAT_INTANGIVEL.SEQ_HISTORICO_MEMORANDO", allocationSize = 1)
@AttributeOverrides({
    @AttributeOverride(name = "dataCadastro", column = @Column(name = "HM_DTHR_CADASTRO")),
    @AttributeOverride(name = "dataAlteracao", column = @Column(name = "HM_DTHR_ALTERACAO")),
    @AttributeOverride(name = "usuarioCadastro", column = @Column(name = "HM_USUARIO_CADASTRO")),
    @AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "HM_USUARIO_ALTERACAO"))
})
public class HistoricoMemorandoEntity extends BaseObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_HISTORICO_MEMORANDO")
    @Column(name = "HM_ID")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "HM_NUMERO_MEMORANDO")
    private String numeroMemorando;

    @Column(name = "HM_URI_ANEXO", length = 500)
    private String uriAnexo;
}
