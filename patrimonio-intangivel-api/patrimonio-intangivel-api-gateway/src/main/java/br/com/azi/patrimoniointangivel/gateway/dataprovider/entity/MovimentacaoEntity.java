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
import java.util.Date;

@Data
@Builder
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_MOVIMENTACAO", schema = "PAT_INTANGIVEL")
@SequenceGenerator(name = "SEQ_MOVIMENTACAO", sequenceName = "PAT_INTANGIVEL.SEQ_MOVIMENTACAO",allocationSize = 1)
@AttributeOverrides({
    @AttributeOverride(name ="dataCadastro" , column = @Column(name = "MO_DTHR_CADASTRO")),
    @AttributeOverride(name = "dataAlteracao", column = @Column(name = "MO_DTHR_ALTERACAO")),
    @AttributeOverride(name = "usuarioCadastro", column = @Column(name = "MO_USUARIO_CADASTRO")),
    @AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "MO_USUARIO_ALTERACAO"))
})
public class MovimentacaoEntity extends BaseObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_MOVIMENTACAO")
    @Column(name = "MO_ID")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "MO_CODIGO")
    private String codigo;

    @Column(name = "MO_TIPO")
    private String tipo;

   @Column(name = "MO_SITUACAO")
   private String situacao;

    @Column(name = "MO_DTHR_ENVIO", columnDefinition = "TIMESTAMP")
    private Date dataDeEnvio;

    @Column(name = "MO_DTHR_FINALIZACAO", columnDefinition = "TIMESTAMP")
    private Date dataDeFinalizacao;

    @Column(name = "MO_MOTIVO_OBS")
    private String motivo;

    @Column(name = "MO_MOTIVO_REJEICAO")
    private String motivoRejeicao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UO_ID_ORGAO_ORIGEM")
    private UnidadeOrganizacionalEntity orgaoOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NLC_ID")
    private NotaLancamentoContabilEntity notaLancamentoContabil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UO_ID_ORGAO_DESTINO")
    private UnidadeOrganizacionalEntity orgaoDestino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PA_ID")
    private PatrimonioEntity patrimonio;

    @Column(name = "MO_USUARIO_FINALIZACAO")
    private String usuarioFinalizacao;
}
