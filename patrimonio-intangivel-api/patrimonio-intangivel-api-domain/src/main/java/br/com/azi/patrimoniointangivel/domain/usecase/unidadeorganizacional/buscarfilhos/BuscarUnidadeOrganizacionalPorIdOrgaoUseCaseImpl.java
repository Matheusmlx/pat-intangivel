package br.com.azi.patrimoniointangivel.domain.usecase.unidadeorganizacional.buscarfilhos;

import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.unidadeorganizacional.buscarfilhos.converter.BuscarUnidadeOrganizacionalPorIdOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BuscarUnidadeOrganizacionalPorIdOrgaoUseCaseImpl implements BuscarUnidadeOrganizacionalPorIdOrgaoUseCase {

    SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    BuscarUnidadeOrganizacionalPorIdOutputDataConverter outputDataConverter;

    @Override
    public BuscarUnidadeOrganizacionalPorIdOrgaoOutputData executar(BuscarUnidadeOrganizacionalPorIdOrgaoInputData inputData) {

        validarDadosEntrada(inputData);

        List<UnidadeOrganizacional> entidadeEncontrada = buscar(inputData);
        List<UnidadeOrganizacional> itens = entidadeEncontrada.stream().sorted(Comparator.comparing(UnidadeOrganizacional::getSigla)).collect(Collectors.toList());
        return outputDataConverter.to(itens);
    }

    private void validarDadosEntrada(BuscarUnidadeOrganizacionalPorIdOrgaoInputData entrada) {
        Validator.of(entrada)
            .validate(BuscarUnidadeOrganizacionalPorIdOrgaoInputData::getId, Objects::nonNull, "O id é nulo")
            .get();
    }

    private List<UnidadeOrganizacional> buscar(BuscarUnidadeOrganizacionalPorIdOrgaoInputData inputData) {
        return sistemaDeGestaoAdministrativaIntegration.buscarUnidadeOrganizacionalPorId(inputData.getId());
    }
}
