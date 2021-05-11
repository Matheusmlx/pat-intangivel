package br.com.azi.patrimoniointangivel.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RotulosPersonalizados {
    private I18n i18n;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class I18n {
        private TelaDadosDeEntrada dadosDeEntradaEdicao;
        private TelaDocumentos documentos;
        private TelaVisualizacao visualizacao;
        private TelaInventario telaInventario;
        private TelaContasContabeis telaContasContabeis;
        private TelaAmortizacaoManual telaAmortizacaoManual;
        private TelaMovimentacao telaMovimentacao;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TelaVisualizacao {
        private CamposVisualizacao campos;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CamposVisualizacao {
        private Campo numeroPatrimonio;
        private Campo tipo;
        private Campo metodo;
        private Campo situacao;
        private Campo taxaAmortizacao;
        private Campo valorAmortizadoMensal;
        private Campo valorAmortizadoAcumulado;
        private Campo valorLiquido;
        private Campo mesAno;
        private Campo orgao;
        private Campo taxa;
        private Campo valorAnteriorAmortizacao;
        private Campo valorAposAmortizacao;
        private Campo valorAmortizado;
        private Campo periodoLicenca;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TelaInventario {
        private CamposInventario campos;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CamposInventario{
        private Campo orgaoResponsavel;
        private Campo mesAnoReferencia;
        private Campo tipo;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TelaContasContabeis {
        private CamposContasContabeis campos;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CamposContasContabeis{
        private Campo codigoContabil;
        private Campo contaContabil;
        private Campo tipoDaConta;
        private Campo metoDeCalculo;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TelaAmortizacaoManual {
        private CamposAmortizacaoManual camposAmortizacaoManual;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CamposAmortizacaoManual{
        private Campo orgaoResponsavel;
        private Campo mesAnoDeReferencia;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TelaDocumentos {
        private CamposDocumentos campos;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CamposDocumentos {
        private Campo numero;
        private Campo data;
        private Campo valor;
        private Campo tipoDocumento;
        private Campo uriAnexo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TelaDadosDeEntrada {
        private CamposDadosDeEntrada campos;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CamposDadosDeEntrada {
        private Campo nome;
        private Campo orgao;
        private Campo setor;
        private Campo descricao;
        private Campo estado;
        private Campo valorAquisicao;
        private Campo valorEntrada;
        private Campo reconhecimento;
        private Campo dataAquisicao;
        private Campo dataAtivacao;
        private Campo dataNL;
        private Campo numeroNL;
        private Campo fornecedor;
        private Campo dataVencimento;
        private Campo contaContabil;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TelaMovimentacao {
        private CamposMovimentacao campos;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CamposMovimentacao {
        private Campo orgaoDestino;
        private Campo motivoObs;
        private Campo tooltip;
        private Campo numeroNL;
        private Campo dataNL;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Campo {
        private String nome;
        private Boolean obrigatorio;
        private String tooltip;
    }

}
