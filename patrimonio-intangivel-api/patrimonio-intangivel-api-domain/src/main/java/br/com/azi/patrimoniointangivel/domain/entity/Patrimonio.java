package br.com.azi.patrimoniointangivel.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Patrimonio {
    private Long id;
    private Tipo tipo;
    private String nome;
    private String numero;
    private String descricao;
    private Situacao situacao;
    private Estado estado;
    private BigDecimal valorLiquido;
    private BigDecimal valorEntrada;
    private BigDecimal valorAquisicao;
    private Reconhecimento reconhecimento;
    private LocalDateTime dataAquisicao;
    private LocalDateTime inicioVidaUtil;
    private LocalDateTime dataVencimento;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAlteracao;
    private String usuarioCadastro;
    private String usuarioAlteracao;
    private Short mesesVidaUtil;
    private LocalDateTime fimVidaUtil;
    private LocalDateTime dataAtivacao;
    private Boolean vidaIndefinida;
    private Boolean ativacaoRetroativa;
    private Boolean amortizavel;
    private LocalDateTime dataFinalAtivacao;
    private Fornecedor fornecedor;
    private UnidadeOrganizacional orgao;
    private UnidadeOrganizacional setor;
    private ContaContabil contaContabil;
    private DadosAmortizacao dadosAmortizacao;
    private Boolean permitirDesativacao;
    private String numeroMemorando;
    private Integer anoMemorando;
    private NotaLancamentoContabil notaLancamentoContabil;

    @AllArgsConstructor
    public enum Estado {
        PRONTO_PARA_USO("Pronto para uso"),
        EM_DESENVOLVIMENTO("Em desenvolvimento");

        private final String valor;

        public String getValor(){ return valor; }
    }

    @AllArgsConstructor
    public enum Situacao {
        EM_ELABORACAO("Em elaboração"),
        ATIVO("Ativo"),
        BAIXADO("Baixado");

        private final String valor;

        public String getValor(){ return valor; }
    }

    @AllArgsConstructor
    public enum Reconhecimento {
        AQUISICAO_SEPARADA("Aquisição Separada"),
        GERACAO_INTERNA("Geração Interna"),
        TRANSACAO_SEM_CONTRAPRESTACAO("Transação Sem Contraprestação");

        private String valor;

        public String getValor(){ return valor; }
    }

    @AllArgsConstructor
    public enum Tipo {
        SOFTWARES("Softwares"),
        DIREITOS_AUTORAIS("Direitos Autorais"),
        LICENCAS("Licenças"),
        MARCAS("Marcas"),
        TITULOS_DE_PUBLICACAO("Títulos de Publicação"),
        RECEITAS_FORMULAS_PROJETOS("Receitas, Fórmulas e Projetos");

        private final String valor;

        public String getValor(){ return valor; }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Filtro extends FiltroBase {
        String conteudo;
        List<Long> unidadeOrganizacionalIds;
    }

}
