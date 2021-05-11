package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.analitico;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.utils.date.DateUtils;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@AllArgsConstructor
public class GerarInventarioAnaliticoUseCaseHelper {

    ConfigContaContabilDataProvider configContaContabilDataProvider;

    LocalDateTime montarDataReferenciaRelatorio(GerarInventarioAnaliticoInputData inputData) {
        String[] dataInput = inputData.getMesReferencia().split("-");
        int ano = Integer.parseInt(dataInput[0]);
        int mes = Integer.parseInt(dataInput[1]);
        return YearMonth.from(LocalDate.of(ano, mes, 1)).atEndOfMonth().atTime(23,59,59);
    }

    LocalDateTime calcularDataFinal (LocalDateTime dataReferencia) {
        if (DateUtils.mesmoMesAno(dataReferencia, LocalDateTime.now())) {
            return YearMonth.from(LocalDate.of(dataReferencia.getYear(), dataReferencia.getMonth(), 1)).atDay(1).atTime(23, 59, 59);
        }
        return dataReferencia;
    }


}
