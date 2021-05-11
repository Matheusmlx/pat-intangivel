package br.com.azi.patrimoniointangivel.domain.usecase.fornecedor.buscarporid;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BuscarFornecedorPorIdOutputData {

    private Long id;
    private String nome;
    private String cpfCnpj;
}
