package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.historicomemorando;

import br.com.azi.patrimoniointangivel.domain.entity.HistoricoMemorando;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.HistoricoMemorandoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.upload.UploadTemporarioInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.upload.UploadTemporarioOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.upload.UploadTemporarioUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.historicomemorando.exception.FalhaEmUploadException;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class GerarHistoricoMemorandoUseCaseImpl implements GerarHistoricoMemorandoUseCase {

    private UploadTemporarioUseCase uploadTemporarioUseCase;

    private HistoricoMemorandoDataProvider historicoMemorandoDataProvider;

    @Override
    public void executar(GerarHistoricoMemorandoInputData inputData) {
        validaDadosEntrada(inputData);
        UploadTemporarioInputData arquivo = criaEntidadeParaUpload(inputData);
        String uri = realizaUpload(arquivo);
        HistoricoMemorando historicoMemorando = criaEntidadeHistorico(inputData.getNumeroMemorando(), uri);
        salvaHistoricoMemorando(historicoMemorando);
    }

    private void validaDadosEntrada(GerarHistoricoMemorandoInputData inputData) {
        Validator.of(inputData)
            .validate(GerarHistoricoMemorandoInputData::getArquivo, Objects::nonNull, "O arquivo é nulo.")
            .validate(GerarHistoricoMemorandoInputData::getNumeroMemorando, Objects::nonNull, "O número do memorando é nulo.")
            .get();
    }

    private String realizaUpload(UploadTemporarioInputData arquivo) {
        UploadTemporarioOutputData outputData = uploadTemporarioUseCase.executar(arquivo);
        if (Objects.nonNull(outputData)) {
            return outputData.getUri();
        }
        throw new FalhaEmUploadException();
    }

    private void salvaHistoricoMemorando(HistoricoMemorando memorando) {
        historicoMemorandoDataProvider.salvar(memorando);
    }

    private UploadTemporarioInputData criaEntidadeParaUpload(GerarHistoricoMemorandoInputData inputData) {
        return new UploadTemporarioInputData(
            inputData.getArquivo().getNome(),
            inputData.getArquivo().getContentType(),
            inputData.getArquivo().getContent());
    }

    private HistoricoMemorando criaEntidadeHistorico(String numero, String uri) {
        return new HistoricoMemorando(numero, uri);
    }
}
