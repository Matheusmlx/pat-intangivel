package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.edicao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditarPatrimonioInputData {

    private Long id;
    private String nome;
    private String descricao;
    private String situacao;
    private String reconhecimento;
    private Date dataAquisicao;
    private Date dataAtivacao;
    private Date dataNL;
    private String numeroNL;
    private Date dataVencimento;
    private String tipo;
    private String estado;
    private Long orgao;
    private Long setor;
    private BigDecimal valorAquisicao;
    private Long fornecedor;
    private Boolean vidaIndefinida;
    private Boolean ativacaoRetroativa;
}
