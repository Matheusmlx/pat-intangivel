package br.com.azi.patrimoniointangivel.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movimentacao {

    private Long id;
    private String codigo;
    private Tipo tipo;
    private String usuarioCadastro;
    private String usuarioFinalizacao;
    private UnidadeOrganizacional orgaoOrigem;
    private UnidadeOrganizacional orgaoDestino;
    private Patrimonio patrimonio;
    private Situacao situacao;
    private LocalDateTime dataDeFinalizacao;
    private LocalDateTime dataDeEnvio;
    private String motivo;
    private String motivoRejeicao;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAlteracao;
    private String numeroMemorando;
    private Integer anoMemorando;
    private NotaLancamentoContabil notaLancamentoContabil;

    @AllArgsConstructor
    public enum Tipo{
        TRANSFERENCIA_DEFINITIVA("Transferência Definitiva"),
        TRANSFERENCIA_TEMPORARIA("Transferência Temporária"),
        DOACAO_ENTRE_ORGAOS("Doação entre Órgãos"),
        DOACAO_PARA_TERCEIROS("Doação para Terceiros");

        private final String valor;
        public  String getValor(){return valor;}

    }

    @AllArgsConstructor
    public enum Situacao{
        EM_ELABORACAO("Em elaboração"),
        AGUARDANDO_RECEBIMENTO("Aguardando recebimento"),
        FINALIZADO("Finalizado"),
        REJEITADO("Rejeitado");

        private final String valor;
        public  String getValor(){return valor;}
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Filtro extends FiltroBase{
        String conteudo;
        String conteudoExtra;
        List<Long> unidadeOrganizacionalIds;
    }

}


