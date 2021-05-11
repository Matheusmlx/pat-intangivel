package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarMovimentacaoPorIdOutputData {

    private Long id;
    private Long patrimonio;
    private String codigo;
    private String tipo;
    private String situacao;
    private String motivo;
    private String motivoRejeicao;
    private UnidadeOrganizacional orgaoDestino;
    private UnidadeOrganizacional orgaoOrigem;
    private String usuarioCadastro;
    private String usuarioFinalizacao;
    private String dataDeFinalizacao;
    private String dataDeEnvio;
    private String dataCadastro;
    private String dataNL;
    private String numeroNL;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UnidadeOrganizacional{
        Long id;
        String sigla;
        String descricao;
    }

}
