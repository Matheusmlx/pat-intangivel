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
public class Documento {
    private Long id;
    private String numero;
    private LocalDateTime data;
    private BigDecimal valor;
    private String uriAnexo;
    private Patrimonio patrimonio;
    private Movimentacao movimentacao;
    private TipoDocumento tipoDocumento;
}


