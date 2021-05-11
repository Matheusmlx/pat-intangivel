package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarlistagem;

import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarporid.BuscarPatrimonioPorIdOutputData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarPatrimoniosOutputData {

    List<Item> items;
    Long totalPages;
    Long totalElements;

    @Data
    @NoArgsConstructor
    public static class Item {
        private Long id;
        private String numero;
        private String nome;
        private String situacao;
        private String estado;
        private String descricao;
        private String orgao;
        private String tipo;
        private String reconhecimento;
        private String dataAquisicao;
        private String dataVencimento;
        private String dataAtivacao;
        private Long fornecedor;
        private Boolean vidaIndefinida;
        private String setor;
        private BigDecimal valorAquisicao;
        private ContaContabil contaContabil;
        private String dataNL;
        private String numeroNL;
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
}
