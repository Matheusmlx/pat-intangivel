package br.com.azi.patrimoniointangivel.gateway.dataprovider.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "VI_RELATORIO_SINTETICO", schema = "PAT_INTANGIVEL")
    public class RelatorioSinteticoEntity {

    @Id
    @Column(name = "RS_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RS_ORGAO")
    private UnidadeOrganizacionalEntity orgao;

    @Column(name = "RS_PATRIMONIO")
    private Long patrimonioId;

    @Column(name = "RS_VALOR_SUBTRA_AMORT", precision = 20, scale = 6)
    private BigDecimal valorAmortizadoMensal;

    @Column(name = "RS_VALOR_LIQUIDO", precision = 20, scale = 6)
    private BigDecimal valorLiquido;

    @Column(name = "RS_VALOR_AQUISICAO", precision = 20, scale = 6)
    private BigDecimal valorAquisicao;

    @Column(name = "RS_PA_ATIVACAO", columnDefinition = "TIMESTAMP")
    private LocalDateTime dataAtivacao;

    @Column(name = "RS_DATA_AMORT", columnDefinition = "TIMESTAMP")
    private LocalDateTime dataCadastro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RS_UO_ID_ORGAO_AMORT")
    private UnidadeOrganizacionalEntity  orgaoAmortizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RS_CONTA_CONTABIL_ID")
    private ContaContabilEntity contaContabil;

}
