package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.visualizacao;


import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
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
public class VisualizarPatrimonioOutputData {

    private Long id;
    private String tipo;
    private String nome;
    private String numero;
    private String descricao;
    private String situacao;
    private String estado;
    private BigDecimal valorLiquido;
    private BigDecimal valorAquisicao;
    private BigDecimal valorEntrada;
    private String reconhecimento;
    private Date dataAquisicao;
    private String dataNL;
    private String numeroNL;
    private Boolean vidaIndefinida;
    private Date inicioVidaUtil;
    private Date dataVencimento;
    private Short mesesVidaUtil;
    private Date fimVidaUtil;
    private Date dataAtivacao;
    private Long fornecedor;
    private UnidadeOrganizacional orgao;
    private UnidadeOrganizacional setor;
    private ContaContabil contaContabil;
    private ConfigAmortizacao configAmortizacao;
    private Periodo periodoVidaUtil;
    private Boolean permitirDesativacao;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UnidadeOrganizacional {
        Long id;
        String sigla;
        String nome;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DadosAmortizacao {
        Long id;
        ConfigAmortizacao configAmortizacao;
        Patrimonio patrimonio;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConfigAmortizacao {
        private Long id;
        private String metodo;
        private Short vidaUtil;
        private String situacao;
        private BigDecimal taxa;
        private BigDecimal percentualResidual;
        private BigDecimal valorAmortizadoMensal;
        private BigDecimal valorAmortizadoAcumulado;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContaContabil {
        Long id;
        String codigo;
        String descricao;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Periodo {
        Integer dia;
        Integer mes;
    }


}
