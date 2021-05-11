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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_LANCAMENTOS_CONTABEIS", schema = "PAT_INTANGIVEL")
@SequenceGenerator(name = "SEQ_LANCAMENTOS_CONTABEIS", sequenceName = "PAT_INTANGIVEL.SEQ_LANCAMENTOS_CONTABEIS", allocationSize = 1)
@AttributeOverrides({
    @AttributeOverride(name = "dataCadastro", column = @Column(name = "LC_DTHR_CADASTRO")),
    @AttributeOverride(name = "dataAlteracao", column = @Column(name = "LC_DTHR_ALTERACAO")),
    @AttributeOverride(name = "usuarioCadastro", column = @Column(name = "LC_USUARIO_CADASTRO")),
    @AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "LC_USUARIO_ALTERACAO"))
})
public class LancamentosContabeisEntity extends BaseObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_LANCAMENTOS_CONTABEIS")
    @Column(name = "LC_ID")
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PA_ID")
    private PatrimonioEntity patrimonio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MO_ID")
    private MovimentacaoEntity movimentacao;

    @Column(name = "LC_DATA", columnDefinition = "TIMESTAMP")
    private LocalDateTime dataLancamento;

    @Column(name = "LC_TIPO_LANCAMENTO")
    private String tipoLancamento;

    @Column(name = "LC_MOTIVO_LANCAMENTO")
    private String motivoLancamento;

    @Column(name = "LC_VALOR_LIQUIDO")
    private BigDecimal valorLiquido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UO_ID_ORGAO")
    private UnidadeOrganizacionalEntity orgao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CC_ID")
    private ContaContabilEntity contaContabil;
}
