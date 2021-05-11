package br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscarmetricasdospatrimoniospororgao;

import br.com.azi.patrimoniointangivel.domain.constant.permissoes.PermissaoPatrimonioConstants;
import br.com.azi.patrimoniointangivel.domain.entity.MetricasPorOrgao;
import br.com.azi.patrimoniointangivel.domain.entity.MetricasPorOrgao.Totalizador;
import br.com.azi.patrimoniointangivel.domain.entity.PatrimoniosAgrupados;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscarmetricasdospatrimoniospororgao.converter.BuscarMetricasDosPatrimoniosPorOrgaoDataConverter;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BuscarMetricasDosPatrimoniosPorOrgaoUseCaseImpl implements BuscarMetricasDosPatrimoniosPorOrgaoUseCase {

    private PatrimonioDataProvider patrimonioDataProvider;

    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    private BuscarMetricasDosPatrimoniosPorOrgaoDataConverter outputDataConverter;

    @Override
    public BuscarMetricasDosPatrimoniosPorOrgaoOutputData executar() {
        List<UnidadeOrganizacional> orgaosQueUsuarioTemAcesso = buscarOrgaosQueUsuarioTemAcesso();
        List<Long> idsDosOrgaosQueUsuarioTemAcesso = gerarIdsDosOrgaosQueUsuarioTemAcesso(orgaosQueUsuarioTemAcesso);
        List<PatrimoniosAgrupados> patrimoniosAgrupados = buscarPatrimoniosAgrupadosPorOrgaoETipo(idsDosOrgaosQueUsuarioTemAcesso);

        List<MetricasPorOrgao> metricasPorOrgaos = gerarMetricasPorOrgao(orgaosQueUsuarioTemAcesso, patrimoniosAgrupados);
        metricasPorOrgaos = removerOrgaosSemPatrimonio(metricasPorOrgaos);

        return outputDataConverter.to(metricasPorOrgaos);
    }

    private List<UnidadeOrganizacional> buscarOrgaosQueUsuarioTemAcesso() {
        List<String> funcoes = new ArrayList<>();
        funcoes.add(PermissaoPatrimonioConstants.NORMAL.getDescription());
        funcoes.add(PermissaoPatrimonioConstants.CONSULTA.getDescription());

        List<UnidadeOrganizacional> unidadeOrganizacionals = sistemaDeGestaoAdministrativaIntegration.buscarUnidadesOrganizacionaisPorFuncao(funcoes);

        return unidadeOrganizacionals
            .stream()
            .distinct()
            .collect(Collectors.toList());
    }

    private List<Long> gerarIdsDosOrgaosQueUsuarioTemAcesso(List<UnidadeOrganizacional> orgaosQueUsuarioTemAcesso) {
        return orgaosQueUsuarioTemAcesso.stream().map(UnidadeOrganizacional::getId).collect(Collectors.toList());
    }

    private List<PatrimoniosAgrupados> buscarPatrimoniosAgrupadosPorOrgaoETipo(List<Long> idsOrgao) {
        return patrimonioDataProvider.buscarPatrimoniosAgrupadosPorOrgaoETipo(idsOrgao);
    }

    private List<MetricasPorOrgao> gerarMetricasPorOrgao(List<UnidadeOrganizacional> orgaosQueUsuarioTemAcesso, List<PatrimoniosAgrupados> patrimoniosAgrupados) {
        List<MetricasPorOrgao> metricasPorOrgaos = orgaosQueUsuarioTemAcesso.stream()
            .map(this::criarMetricasPorOrgao)
            .sorted(Comparator.comparing(MetricasPorOrgao::getSigla))
            .distinct()
            .collect(Collectors.toList());

        metricasPorOrgaos.forEach(metricasPorOrgao -> inserirTotalizadorNosOrgaos(metricasPorOrgao, patrimoniosAgrupados));

        return metricasPorOrgaos;
    }

    private MetricasPorOrgao criarMetricasPorOrgao(UnidadeOrganizacional unidadeOrganizacional) {
        return MetricasPorOrgao.builder()
            .idOrgao(unidadeOrganizacional.getId())
            .nome(unidadeOrganizacional.getNome())
            .sigla(unidadeOrganizacional.getSigla())
            .build();
    }

    private void inserirTotalizadorNosOrgaos(MetricasPorOrgao metricasPorOrgao, List<PatrimoniosAgrupados> patrimoniosAgrupados) {
        metricasPorOrgao.setTipos(
            patrimoniosAgrupados.stream()
                .filter(patrimonioAgrupado -> patrimonioAgrupado.getIdOrgao().equals(metricasPorOrgao.getIdOrgao()))
                .map(this::criarTotalizador)
                .sorted(Comparator.comparing(Totalizador::getNome))
                .collect(Collectors.toList())
        );

        metricasPorOrgao.setTotal(0L);
        metricasPorOrgao.getTipos().forEach(totalizador -> metricasPorOrgao.setTotal(metricasPorOrgao.getTotal() + totalizador.getQuantidade()));
    }

    private Totalizador criarTotalizador(PatrimoniosAgrupados patrimoniosAgrupados) {
        return Totalizador
            .builder()
            .nome(patrimoniosAgrupados.getTipo())
            .quantidade(patrimoniosAgrupados.getContador())
            .build();
    }

    private List<MetricasPorOrgao> removerOrgaosSemPatrimonio(List<MetricasPorOrgao> metricasPorOrgaos) {
        return metricasPorOrgaos.stream()
            .filter(metricasPorOrgao -> !metricasPorOrgao.getTipos().isEmpty())
            .collect(Collectors.toList());
    }
}
