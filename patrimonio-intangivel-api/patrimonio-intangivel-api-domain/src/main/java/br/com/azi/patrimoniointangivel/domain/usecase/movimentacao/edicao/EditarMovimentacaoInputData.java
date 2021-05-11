package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.edicao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditarMovimentacaoInputData {
    private Long id;
    private Long patrimonio;
    private String motivo;
    private Long orgaoDestino;
    private Date dataNL;
    private String numeroNL;

}
