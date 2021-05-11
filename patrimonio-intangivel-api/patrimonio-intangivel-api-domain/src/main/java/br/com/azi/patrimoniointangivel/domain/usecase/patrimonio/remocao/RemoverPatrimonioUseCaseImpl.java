package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.remocao;

import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.remocao.exception.PatrimonioNaoPodeSerExcluidoException;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class RemoverPatrimonioUseCaseImpl implements RemoverPatrimonioUseCase {

    private PatrimonioDataProvider patrimonioDataProvider;

    private DocumentoDataProvider documentoDataProvider;

    @Override
    public void executar(RemoverPatrimonioInputData inputData) {

        validarDadosEntrada(inputData);
        Patrimonio patrimonio = buscarPatrimonio(inputData);
        validarSituacaoPatrimonio(patrimonio);
        removeDocumentos(patrimonio);

        remover(inputData);
    }

    private void removeDocumentos(Patrimonio patrimonio) {
        documentoDataProvider.removeDocumentosPorPatrimonioId(patrimonio.getId());
    }

    private void validarDadosEntrada(RemoverPatrimonioInputData entrada) {
        Validator.of(entrada)
            .validate(RemoverPatrimonioInputData::getId, Objects::nonNull, "O id da Patrimônio é nulo")
            .get();
    }

    private Patrimonio buscarPatrimonio(RemoverPatrimonioInputData inputData) {
        Optional<Patrimonio> patrimonio = patrimonioDataProvider.buscarPorId(inputData.getId());
        return patrimonio.orElseThrow(PatrimonioNaoEncontradoException::new);
    }

    private void validarSituacaoPatrimonio(Patrimonio patrimonio) {
        if (patrimonio.getSituacao().equals(Patrimonio.Situacao.ATIVO)) {
            throw new PatrimonioNaoPodeSerExcluidoException();
        }
    }

    private void remover(RemoverPatrimonioInputData inputData) {
        patrimonioDataProvider.remover(inputData.getId());
    }
}
