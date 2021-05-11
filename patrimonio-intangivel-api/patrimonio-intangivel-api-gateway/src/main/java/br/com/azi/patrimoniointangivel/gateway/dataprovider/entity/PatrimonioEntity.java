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
@Table(name = "TB_PATRIMONIO", schema = "PAT_INTANGIVEL")
@SequenceGenerator(name = "SEQ_PATRIMONIO", sequenceName = "PAT_INTANGIVEL.SEQ_PATRIMONIO", allocationSize = 1)
@AttributeOverrides({
    @AttributeOverride(name = "dataCadastro", column = @Column(name = "PA_DTHR_CADASTRO")),
    @AttributeOverride(name = "dataAlteracao", column = @Column(name = "PA_DTHR_ALTERACAO")),
    @AttributeOverride(name = "usuarioCadastro", column = @Column(name = "PA_USUARIO_CADASTRO")),
    @AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "PA_USUARIO_ALTERACAO"))
})
public class PatrimonioEntity extends BaseObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_PATRIMONIO")
    @Column(name = "PA_ID")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "PA_TIPO", length = 45, nullable = false)
    private String tipo;

    @Column(name = "PA_NOME")
    private String nome;

    @Column(name = "PA_NUMERO")
    private String numero;

    @Column(name = "PA_DESCRICAO")
    private String descricao;

    @Column(name = "PA_SITUACAO", length = 45)
    private String situacao;

    @Column(name = "PA_ESTADO", length = 45)
    private String estado;

    @Column(name = "PA_VALOR_LIQUIDO", precision = 20, scale = 6)
    private BigDecimal valorLiquido;

    @Column(name = "PA_VALOR_ENTRADA", precision = 20, scale = 6)
    private BigDecimal valorDeEntrada;

    @Column(name = "PA_VALOR_AQUISICAO", precision = 20, scale = 6)
    private BigDecimal valorAquisicao;

    @Column(name = "PA_RECONHECIMENTO", length = 45)
    private String reconhecimento;

    @Column(name = "PA_DTHR_AQUISICAO", columnDefinition = "TIMESTAMP")
    private Date dataAquisicao;

    @Column(name = "PA_DTHR_INICIO_VIDA_UTIL", columnDefinition = "TIMESTAMP")
    private Date inicioVidaUtil;

    @Column(name = "PA_DTHR_VENCIMENTO", columnDefinition = "TIMESTAMP")
    private Date dataVencimento;

    @Column(name = "PA_MESES_VIDA_UTIL")
    private Short mesesVidaUtil;

    @Column(name = "PA_DTHR_FIM_VIDA_UTIL", columnDefinition = "TIMESTAMP")
    private Date fimVidaUtil;

    @Column(name = "PA_VIDA_INDEFINIDA")
    private Boolean vidaIndefinida;

    @Column(name = "PA_ATIVACAO_RETROATIVA")
    private Boolean ativacaoRetroativa;

    @Column(name = "PA_DTHR_ATIVACAO", columnDefinition = "TIMESTAMP")
    private Date dataAtivacao;

    @Column(name = "PA_AMORTIZAVEL")
    private Boolean amortizavel;
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "NLC_ID")
    private NotaLancamentoContabilEntity notaLancamentoContabil;

    @Column(name = "PA_DTHR_FINAL_ATIVACAO", columnDefinition = "TIMESTAMP")
    private Date dataFinalAtivacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UO_ID_ORGAO")
    private UnidadeOrganizacionalEntity orgao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UO_ID_SETOR")
    private UnidadeOrganizacionalEntity setor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CC_ID")
    private ContaContabilEntity contaContabil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PE_ID")
    private FornecedorEntity fornecedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DA_ID")
    private DadosAmortizacaoEntity dadosAmortizacao;

    @Column(name = "PA_NUMERO_MEMORANDO")
    private String numeroMemorando;

    @Column(name = "PA_ANO_MEMORANDO")
    private Integer anoMemorando;

    public PatrimonioEntity(Long id) {
        this.id = id;
    }

}
