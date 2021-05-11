package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.deletar;

import br.com.azi.patrimoniointangivel.domain.entity.Documento;
import br.com.azi.patrimoniointangivel.domain.exception.DocumentoNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.deletar.exception.DocumentoNaoPertenceAoPatrimonioInformadoException;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class DeletarDocumentoUseCaseImpl implements DeletarDocumentoUseCase {

    private DocumentoDataProvider documentoDataProvider;

    @Override
    public void executar(DeletarDocumentoInputData inputData) {
        validarDadosEntrada(inputData);

        Documento documento = buscar(inputData);
        validarDocumentoPertenceAoPatrimonio(inputData, documento);

        remover(documento);
    }

    private void validarDadosEntrada(DeletarDocumentoInputData entrada) {
        Validator.of(entrada)
            .validate(DeletarDocumentoInputData::getId, Objects::nonNull, "O id é nulo!")
            .get();
    }

    private Documento buscar(DeletarDocumentoInputData inputData) {
        Optional<Documento> documento = documentoDataProvider.buscarPorId(inputData.getId());
        return documento.orElseThrow(DocumentoNaoEncontradoException::new);
    }

    private void validarDocumentoPertenceAoPatrimonio(DeletarDocumentoInputData inputData, Documento documento) {
        if (!documento.getPatrimonio().getId().equals(inputData.getPatrimonioId())) {
            throw new DocumentoNaoPertenceAoPatrimonioInformadoException();
        }
    }

    private void remover(Documento documento) {
        documentoDataProvider.remover(documento.getId());
    }
}
