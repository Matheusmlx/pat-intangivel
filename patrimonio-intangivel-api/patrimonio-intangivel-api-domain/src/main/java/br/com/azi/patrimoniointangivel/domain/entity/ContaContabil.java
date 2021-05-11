package br.com.azi.patrimoniointangivel.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ContaContabil {

    private Long id;
    private String codigo;
    private String descricao;
    private Situacao situacao;
    private ConfigContaContabil configContaContabil;

    public enum Situacao {
        ATIVO,
        INATIVO
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Filtro extends FiltroBase {
        String conteudo;
        Long produtoId;
    }
}

