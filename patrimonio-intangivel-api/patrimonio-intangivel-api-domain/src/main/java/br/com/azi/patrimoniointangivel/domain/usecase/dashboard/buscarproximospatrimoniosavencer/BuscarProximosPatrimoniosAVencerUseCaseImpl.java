package br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscarproximospatrimoniosavencer;

import br.com.azi.patrimoniointangivel.domain.constant.permissoes.PermissaoPatrimonioConstants;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.PatrimonioComDiasParaVencer;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscarproximospatrimoniosavencer.converter.BuscarProximosPatrimoniosAVencerOutputDataConverter;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BuscarProximosPatrimoniosAVencerUseCaseImpl implements BuscarProximosPatrimoniosAVencerUseCase {

    private PatrimonioDataProvider patrimonioDataProvider;

    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    private Clock clock;

    private BuscarProximosPatrimoniosAVencerOutputDataConverter outputDataConverter;

    @Override
    public BuscarProximosPatrimoniosAVencerOutputData executar(BuscarProximosPatrimoniosAVencerInputData inputData) {
        validarDadosEntrada(inputData);

        List<Long> idsOrgaosDoUsuario = buscarOrgaosIdsAcessoUsuario();
        List<Patrimonio> patrimoniosQueIraoVencer = buscarProximosPatrimoniosAVencerNosOrgaos(idsOrgaosDoUsuario, inputData.getQuantidadeDeRegistros());

        return outputDataConverter.to(gerarPatrimonioComDiasParaVencer(patrimoniosQueIraoVencer));
    }

    private void validarDadosEntrada(BuscarProximosPatrimoniosAVencerInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarProximosPatrimoniosAVencerInputData::getQuantidadeDeRegistros, Objects::nonNull, "Quantidade de registros não informado!")
            .get();
    }

    private List<Long> buscarOrgaosIdsAcessoUsuario() {
        List<String> funcoes = new ArrayList<>();
        funcoes.add(PermissaoPatrimonioConstants.NORMAL.getDescription());
        funcoes.add(PermissaoPatrimonioConstants.CONSULTA.getDescription());

        List<UnidadeOrganizacional> unidadeOrganizacionals = sistemaDeGestaoAdministrativaIntegration.buscarUnidadesOrganizacionaisPorFuncao(funcoes);

        return unidadeOrganizacionals
            .stream()
            .map(UnidadeOrganizacional::getId)
            .distinct()
            .collect(Collectors.toList());
    }

    private List<Patrimonio> buscarProximosPatrimoniosAVencerNosOrgaos(List<Long> idsOrgao, Long numeroDeRegistrosRetornados) {
        return patrimonioDataProvider.buscarProximosPatrimoniosAVencerNosOrgaos(idsOrgao, numeroDeRegistrosRetornados);
    }

    private List<PatrimonioComDiasParaVencer> gerarPatrimonioComDiasParaVencer(List<Patrimonio> patrimonios) {
        return patrimonios.stream()
            .map(
                patrimonio -> new PatrimonioComDiasParaVencer(
                    patrimonio.getId(),
                    patrimonio.getNome(),
                    calcularDiasParaVencer(patrimonio.getFimVidaUtil())
                )
            )
            .collect(Collectors.toList());
    }

    private long calcularDiasParaVencer(LocalDateTime dataVencimento) {
        return ChronoUnit.DAYS.between(
            LocalDateTime.ofInstant(LocalDateTime.now(clock).toInstant(ZoneOffset.UTC), ZoneOffset.UTC),
            LocalDateTime.ofInstant(dataVencimento.toInstant(ZoneOffset.UTC), ZoneOffset.UTC));
    }
}
