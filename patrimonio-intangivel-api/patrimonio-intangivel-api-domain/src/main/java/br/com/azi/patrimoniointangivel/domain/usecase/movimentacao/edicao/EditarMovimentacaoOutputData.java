package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.edicao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditarMovimentacaoOutputData {
    private Long id;
    private String motivo;
    private Long orgaoDestino;
    private String dataNL;
    private String numeroNL;
}
