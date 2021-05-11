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

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_CONFIG_AMORTIZACAO", schema = "PAT_INTANGIVEL")
@SequenceGenerator(name = "SEQ_CONFIG_DEPRECIACAO", sequenceName = "PAT_INTANGIVEL.SEQ_CONFIG_DEPRECIACAO", allocationSize = 1)
@AttributeOverrides({
    @AttributeOverride(name = "dataCadastro", column = @Column(name = "CA_DTHR_CADASTRO")),
    @AttributeOverride(name = "dataAlteracao", column = @Column(name = "CA_DTHR_ALTERACAO")),
    @AttributeOverride(name = "usuarioCadastro", column = @Column(name = "CA_USUARIO_CADASTRO")),
    @AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "CA_USUARIO_ALTERACAO"))
})
public class ConfigAmortizacaoEntity extends BaseObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "PAT_INTANGIVEL.SEQ_CONFIG_DEPRECIACAO")
    @Column(name = "CA_ID")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "CA_METODO")
    private String metodo;

    @Column(name = "CA_VIDA_UTIL_MESES")
    private Short vidaUtil;

    @Column(name = "CA_SITUACAO")
    private String situacao;

    @Column(name = "CA_TIPO")
    private String tipo;

    @Column(name = "CA_TAXA")
    private BigDecimal taxa;

    @Column(name = "CA_PERCENTUAL_RESIDUAL")
    private BigDecimal percentualResidual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CC_ID")
    private ContaContabilEntity contaContabil;

}
