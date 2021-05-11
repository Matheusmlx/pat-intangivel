package br.com.azi.patrimoniointangivel.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LancamentosContabeis {

    private Long id;
    private Patrimonio patrimonio;
    private Movimentacao movimentacao;
    private TipoLancamento tipoLancamento;
    private MotivoLancamento motivoLancamento;
    private BigDecimal valorLiquido;
    private UnidadeOrganizacional orgao;
    private ContaContabil contaContabil;
    private LocalDateTime dataLancamento;

    @AllArgsConstructor
    public enum TipoLancamento {
        CREDITO("Crédito"),
        DEBITO("Débito");

        private final String valor;

        public String getValor() {
            return valor;
        }
    }

    @AllArgsConstructor
    public enum MotivoLancamento {
        CANCELAMENTO_ATIVACAO("Cancelamento ativação"),
        ATIVACAO("Ativação"),
        TRANSFERENCIA_DEFINITIVA("Transferência Definitiva"),
        DOACAO_ENTRE_ORGAOS("Doação entre Órgãos");

        private final String valor;

        public String getValor() {
            return valor;
        }
    }
}
