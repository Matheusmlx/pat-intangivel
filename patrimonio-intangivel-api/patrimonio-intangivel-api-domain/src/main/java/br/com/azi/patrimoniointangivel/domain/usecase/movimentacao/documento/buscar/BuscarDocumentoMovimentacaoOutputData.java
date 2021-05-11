package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.buscar;

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
public class BuscarDocumentoMovimentacaoOutputData {

    List<Documento> items;

    @Data
    @NoArgsConstructor
    public static class Documento {
        private Long id;
        private String numero;
        private String data;
        private BigDecimal valor;
        private String uriAnexo;
        private Long idTipoDocumento;
        private Long idMovimentacao;
        private Long idPatrimonio;
    }
}
