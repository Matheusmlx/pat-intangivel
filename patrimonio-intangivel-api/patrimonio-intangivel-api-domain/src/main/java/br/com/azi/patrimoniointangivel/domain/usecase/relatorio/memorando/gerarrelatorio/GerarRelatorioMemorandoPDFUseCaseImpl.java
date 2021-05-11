package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.gerarrelatorio;

import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;
import br.com.azi.patrimoniointangivel.domain.entity.LancamentosContabeis;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.RelatorioMemorando;
import br.com.azi.patrimoniointangivel.domain.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.LancamentosContabeisDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.memorando.GerarNumeroMemorandoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.memorando.GerarNumeroMemorandoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.gerarrelatorio.converter.GerarRelatorioMemorandoPDFOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.historicomemorando.GerarHistoricoMemorandoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.historicomemorando.GerarHistoricoMemorandoUseCase;
import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class GerarRelatorioMemorandoPDFUseCaseImpl implements GerarRelatorioMemorandoPDFUseCase {

    private PatrimonioDataProvider patrimonioDataProvider;

    private SistemaDeRelatoriosIntegration sistemaDeRelatoriosIntegration;

    private GerarRelatorioMemorandoPDFOutputDataConverter outputDataConverter;

    private GerarNumeroMemorandoUseCase gerarNumeroMemorandoUseCase;

    private GerarHistoricoMemorandoUseCase gerarHistoricoMemorandoUseCase;

    private LancamentosContabeisDataProvider lancamentosContabeisDataProvider;

    @Override
    public GerarRelatorioMemorandoPDFOutputData executar( GerarRelatorioMemorandoPDFInputData inputData) {
        validaDadosEntrada(inputData);

        Patrimonio patrimonio = buscaPatrimonio(inputData);
        LancamentosContabeis lancamentoContabil = buscarLancamentoContabilAtivacao(inputData);

        verificaNumeroMemorando(patrimonio);

        RelatorioMemorando relatorioMemorando = preparaRelatorioMemorando(patrimonio, lancamentoContabil);

        Arquivo arquivo = sistemaDeRelatoriosIntegration.gerarRelatorioMemorandoPDF(relatorioMemorando);

        geraHistoricoMemorando(relatorioMemorando.getNumeroAno(),arquivo);

        return outputDataConverter.to(arquivo);
    }

    private void validaDadosEntrada(GerarRelatorioMemorandoPDFInputData inputData){
        Validator.of(inputData)
            .validate(GerarRelatorioMemorandoPDFInputData::getPatrimonioId, Objects::nonNull, "O id do patrimônio é nulo.")
            .get();
    }

    private Patrimonio buscaPatrimonio(GerarRelatorioMemorandoPDFInputData inputData) {
        Optional<Patrimonio> entidadePatrimonio = patrimonioDataProvider.buscarPorId(inputData.getPatrimonioId());
        return entidadePatrimonio.orElseThrow(PatrimonioNaoEncontradoException::new);
    }

    private LancamentosContabeis buscarLancamentoContabilAtivacao(GerarRelatorioMemorandoPDFInputData inputData) {
        Optional<LancamentosContabeis> entidadePatrimonio = lancamentosContabeisDataProvider.buscarLancamentoContabilDeAtivacaoDoPatrimonio(inputData.getPatrimonioId());
        if (entidadePatrimonio.isPresent()) {
            return entidadePatrimonio.get();
        } else {
            return null;
        }
    }

    private void verificaNumeroMemorando(Patrimonio patrimonio){
        if(Objects.isNull(patrimonio.getAnoMemorando()) && Objects.isNull(patrimonio.getNumeroMemorando())){
            gerarNumeroMemorando(patrimonio);
            salvaPatrimonioComMemorando(patrimonio);
        }
    }

    private RelatorioMemorando preparaRelatorioMemorando(Patrimonio patrimonio, LancamentosContabeis lancamentoContabil){
           return RelatorioMemorando.builder()
               .numeroAno(patrimonio.getNumeroMemorando() + "/" + patrimonio.getAnoMemorando())
               .mesAno(Objects.nonNull(lancamentoContabil) ? criaDataRelatorio(lancamentoContabil.getDataLancamento()) : " ")
                .patrimonio(RelatorioMemorando.Patrimonio.builder()
                    .numero(Objects.nonNull(patrimonio.getNumero()) ? patrimonio.getNumero() : "-")
                    .nome(Objects.nonNull(patrimonio.getNome()) ? patrimonio.getNome() : "-")
                    .dataNL(Objects.nonNull(patrimonio.getNotaLancamentoContabil()) ? retornaDataString(patrimonio.getNotaLancamentoContabil().getDataLancamento()) : "-")
                    .numeroNL(Objects.nonNull(patrimonio.getNotaLancamentoContabil()) && Objects.nonNull(patrimonio.getNotaLancamentoContabil().getNumero())? patrimonio.getNotaLancamentoContabil().getNumero() : "-")
                    .descricao(Objects.nonNull(patrimonio.getDescricao()) ? patrimonio.getDescricao() : "-")
                    .dataAtivacao(retornaDataString(patrimonio.getDataAtivacao()))
                    .valorAquisicao(patrimonio.getValorAquisicao())
                    .valorEntrada(patrimonio.getValorEntrada())
                    .build())
               .contaContabil(RelatorioMemorando.ContaContabil.builder()
                   .nome(Objects.nonNull(patrimonio.getContaContabil()) ? patrimonio.getContaContabil().getDescricao() : "-")
                   .build())
               .fornecedor(RelatorioMemorando.Fornecedor.builder()
                   .nome( Objects.nonNull(patrimonio.getFornecedor()) ? patrimonio.getFornecedor().getNome() : "-")
                   .build())
               .reconhecimento(RelatorioMemorando.Reconhecimento.builder()
                   .nome(Objects.nonNull(patrimonio.getReconhecimento()) ? patrimonio.getReconhecimento().getValor() : "-")
                   .build())
               .orgao(RelatorioMemorando.Orgao.builder()
                   .nome(Objects.nonNull(patrimonio.getOrgao()) ? patrimonio.getOrgao().getNome() + " - " + patrimonio.getOrgao().getSigla() : "-")
                   .build())
               .setor(RelatorioMemorando.Setor.builder()
                   .nome(Objects.nonNull(patrimonio.getSetor()) ? patrimonio.getSetor().getNome() + " - " + patrimonio.getSetor().getSigla() : "-")
                   .build())
                .build();
    }

    private void geraHistoricoMemorando(String numero, Arquivo arquivo){
        gerarHistoricoMemorandoUseCase.executar(new GerarHistoricoMemorandoInputData(numero,arquivo));
    }

    private String criaDataRelatorio(LocalDateTime data){
        String mes = data.getMonth().getDisplayName(TextStyle.FULL, new Locale("pt"));
        String ano = String.valueOf(data.getYear());
        return StringUtils.capitalize(mes)+ "/" + ano;
    }

    private String retornaDataString(LocalDateTime data){
        if(Objects.nonNull(data)){
            return data.getDayOfMonth() + "/" + data.getMonthValue() + "/" + data.getYear();
        }
        return "-";
    }

    private void gerarNumeroMemorando(Patrimonio patrimonio) {
        GerarNumeroMemorandoOutputData gerarNumeroMemorandoOutputData = gerarNumeroMemorandoUseCase.executar();
        patrimonio.setNumeroMemorando(gerarNumeroMemorandoOutputData.getNumeroMemorando());
        patrimonio.setAnoMemorando(Calendar.getInstance().get(Calendar.YEAR));
    }

    private void salvaPatrimonioComMemorando(Patrimonio patrimonio) {
        patrimonioDataProvider.atualizar(patrimonio);
    }
}
