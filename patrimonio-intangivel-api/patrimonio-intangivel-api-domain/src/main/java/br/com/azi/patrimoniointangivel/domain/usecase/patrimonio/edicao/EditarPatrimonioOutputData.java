package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.edicao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditarPatrimonioOutputData {
    private Long id;
    private String nome;
    private String descricao;
    private String situacao;
    private String reconhecimento;
    private String dataAquisicao;
    private Long fornecedor;
    private String dataVencimento;
    private String tipo;
    private String estado;
    private Boolean vidaIndefinida;
    private Long orgao;
    private Long setor;
    private BigDecimal valorAquisicao;
    private ContaContabil contaContabil;
    private String dataAtivacao;
    private String numeroNL;
    private String dataNL;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContaContabil {
        Long id;
        String codigo;
        String descricao;
    }

}
