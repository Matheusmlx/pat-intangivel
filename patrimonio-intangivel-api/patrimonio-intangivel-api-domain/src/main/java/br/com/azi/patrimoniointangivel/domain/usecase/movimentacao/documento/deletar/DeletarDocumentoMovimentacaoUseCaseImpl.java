package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.deletar;

import br.com.azi.patrimoniointangivel.domain.entity.Documento;
import br.com.azi.patrimoniointangivel.domain.exception.DocumentoNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.deletar.exception.DocumentoNaoPertenceAMovimentacaoInformadoException;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class DeletarDocumentoMovimentacaoUseCaseImpl implements DeletarDocumentoMovimentacaoUseCase {

    private DocumentoDataProvider documentoDataProvider;

    @Override
    public void executar(DeletarDocumentoMovimentacaoInputData inputData) {
        validarDadosEntrada(inputData);

        Documento documento = buscarDocumento(inputData);
        validarDocumentoPertenceAMovimentacao(inputData, documento);

        remover(documento);
    }

    private void validarDadosEntrada(DeletarDocumentoMovimentacaoInputData inputData) {
        Validator.of(inputData)
            .validate(DeletarDocumentoMovimentacaoInputData::getId, Objects::nonNull, "O id é nulo!")
            .get();
    }

    private Documento buscarDocumento(DeletarDocumentoMovimentacaoInputData inputData) {
        Optional<Documento> documento = documentoDataProvider.buscarPorId(inputData.getId());
        return documento.orElseThrow(DocumentoNaoEncontradoException::new);
    }

    private void validarDocumentoPertenceAMovimentacao(DeletarDocumentoMovimentacaoInputData inputData, Documento documento) {
        if (!documento.getMovimentacao().getId().equals(inputData.getMovimentacaoId())) {
            throw new DocumentoNaoPertenceAMovimentacaoInformadoException();
        }
    }

    private void remover(Documento documento) {
        documentoDataProvider.remover(documento.getId());
    }
}
