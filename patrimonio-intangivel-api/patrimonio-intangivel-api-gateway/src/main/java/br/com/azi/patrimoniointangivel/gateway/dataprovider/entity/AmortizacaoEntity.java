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
import java.util.Date;

@Data
@Builder
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_AMORTIZACAO", schema = "PAT_INTANGIVEL")
@SequenceGenerator(name = "SEQ_AMORTIZACAO", sequenceName = "PAT_INTANGIVEL.SEQ_AMORTIZACAO", allocationSize = 1)
@AttributeOverrides({
    @AttributeOverride(name = "dataCadastro", column = @Column(name = "AM_DTHR_CADASTRO")),
    @AttributeOverride(name = "dataAlteracao", column = @Column(name = "AM_DTHR_ALTERACAO")),
    @AttributeOverride(name = "usuarioCadastro", column = @Column(name = "AM_USUARIO_CADASTRO")),
    @AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "AM_USUARIO_ALTERACAO"))
})
public class AmortizacaoEntity extends BaseObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_AMORTIZACAO")
    @Column(name = "AM_ID")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "AM_DTHR_INICIAL", columnDefinition = "TIMESTAMP")
    private Date dataInicial;

    @Column(name = "AM_DTHR_FINAL", columnDefinition = "TIMESTAMP")
    private Date dataFinal;

    @Column(name = "AM_VALOR_ANTERIOR", precision = 20, scale = 6)
    private BigDecimal valorAnterior;

    @Column(name = "AM_VALOR_POSTERIOR", precision = 20, scale = 6)
    private BigDecimal valorPosterior;

    @Column(name = "AM_VALOR_SUBTRAIDO", precision = 20, scale = 6)
    private BigDecimal valorSubtraido;

    @Column(name = "AM_TAXA_APLICADA", precision = 20, scale = 6)
    private BigDecimal taxaAplicada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PA_ID")
    private PatrimonioEntity patrimonio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CA_ID")
    private ConfigAmortizacaoEntity configAmortizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UO_ID_ORGAO")
    private UnidadeOrganizacionalEntity orgao;
}
