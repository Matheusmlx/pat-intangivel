package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.cadastro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CadastraPatrimonioOutputData {

    private Long id;
    private String nome;
    private String situacao;
    private String tipo;
}
