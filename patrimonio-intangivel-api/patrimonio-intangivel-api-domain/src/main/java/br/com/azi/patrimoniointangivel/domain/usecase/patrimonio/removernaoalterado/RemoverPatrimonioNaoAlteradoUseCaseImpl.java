package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.removernaoalterado;

import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.remocao.RemoverPatrimonioInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.remocao.RemoverPatrimonioUseCase;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class RemoverPatrimonioNaoAlteradoUseCaseImpl implements RemoverPatrimonioNaoAlteradoUseCase {

    private PatrimonioDataProvider patrimonioDataProvider;

    private RemoverPatrimonioUseCase removerPatrimonioUseCase;

    @Override
    public void executar(RemoverPatrimonioNaoAlteradoInputData inputData) {
        validarDadosEntrada(inputData);
        Patrimonio patrimonio = buscarPatrimonio(inputData);
        if (!verificarSePatrimonioFoiAlterado(patrimonio)) {
            remover(inputData);
        }
    }

    private void validarDadosEntrada(RemoverPatrimonioNaoAlteradoInputData entrada) {
        Validator.of(entrada)
            .validate(RemoverPatrimonioNaoAlteradoInputData::getId, Objects::nonNull, "O id da Patrimônio é nulo")
            .get();
    }

    private Patrimonio buscarPatrimonio(RemoverPatrimonioNaoAlteradoInputData inputData) {
        Optional<Patrimonio> patrimonio = patrimonioDataProvider.buscarPorId(inputData.getId());
        return patrimonio.orElseThrow(() -> new PatrimonioNaoEncontradoException());
    }

    private boolean verificarSePatrimonioFoiAlterado(Patrimonio patrimonio) {
        return Objects.nonNull(patrimonio.getNome())
            || Objects.nonNull(patrimonio.getDescricao())
            || (Objects.nonNull(patrimonio.getOrgao()) && Objects.nonNull(patrimonio.getOrgao().getId()))
            || (Objects.nonNull(patrimonio.getSetor()) && Objects.nonNull(patrimonio.getSetor().getId()))
            || Objects.nonNull(patrimonio.getEstado())
            || Objects.nonNull(patrimonio.getValorAquisicao())
            || Objects.nonNull(patrimonio.getReconhecimento())
            || Objects.nonNull(patrimonio.getDataAquisicao())
            || Objects.nonNull(patrimonio.getNotaLancamentoContabil())
            || Objects.nonNull(patrimonio.getVidaIndefinida())
            || (Objects.nonNull(patrimonio.getFornecedor()) && Objects.nonNull(patrimonio.getFornecedor().getId()));
    }

    private void remover(RemoverPatrimonioNaoAlteradoInputData inputData) {
        RemoverPatrimonioInputData removerPatrimonioInputData = RemoverPatrimonioInputData
            .builder()
            .id(inputData.getId())
            .build();
        removerPatrimonioUseCase.executar(removerPatrimonioInputData);
    }
}
