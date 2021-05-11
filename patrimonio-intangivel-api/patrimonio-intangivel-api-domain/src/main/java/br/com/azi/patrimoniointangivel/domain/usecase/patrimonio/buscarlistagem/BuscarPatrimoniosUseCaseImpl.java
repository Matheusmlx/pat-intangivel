package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarlistagem;

import br.com.azi.patrimoniointangivel.domain.constant.permissoes.PermissaoPatrimonioConstants;
import br.com.azi.patrimoniointangivel.domain.entity.ListaPaginada;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarlistagem.converter.BuscarPatrimoniosFiltroConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarlistagem.converter.BuscarPatrimoniosOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarlistagem.exception.FiltroIncompletoException;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BuscarPatrimoniosUseCaseImpl implements BuscarPatrimoniosUseCase {

    private PatrimonioDataProvider patrimonioDataProvider;

    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    private BuscarPatrimoniosFiltroConverter buscarPatrimoniosFiltroConverter;

    private BuscarPatrimoniosOutputDataConverter outputDataConverter;

    @Override
    public BuscarPatrimoniosOutputData executar(BuscarPatrimoniosInputData inputData) {
        validarDadosEntrada(inputData);

        Patrimonio.Filtro filtro = criarFiltro(inputData);
        ListaPaginada<Patrimonio> entidadesEncontradas = buscar(filtro);

        return outputDataConverter.to(entidadesEncontradas);
    }

    private void validarDadosEntrada(BuscarPatrimoniosInputData entrada) {
        Validator.of(entrada)
            .validate(BuscarPatrimoniosInputData::getSize, size -> Objects.nonNull(size) && size > 0, new FiltroIncompletoException("Ausência da quantidade de registros por página."))
            .validate(BuscarPatrimoniosInputData::getPage, Objects::nonNull, new FiltroIncompletoException("Ausência do número da página."))
            .get();
    }

    private Patrimonio.Filtro criarFiltro(BuscarPatrimoniosInputData inputData) {
        Patrimonio.Filtro filtro = buscarPatrimoniosFiltroConverter.to(inputData);
        filtro.setUnidadeOrganizacionalIds(buscarOrgaosIdsAcessoUsuario());
        return filtro;
    }

    private List<Long> buscarOrgaosIdsAcessoUsuario() {
        List<String> funcoes = new ArrayList<>();
        funcoes.add(PermissaoPatrimonioConstants.NORMAL.getDescription());
        funcoes.add(PermissaoPatrimonioConstants.CONSULTA.getDescription());

        List<UnidadeOrganizacional> unidadeOrganizacionals = sistemaDeGestaoAdministrativaIntegration.buscarUnidadesOrganizacionaisPorFuncao(funcoes);

        return unidadeOrganizacionals
            .stream()
            .map(UnidadeOrganizacional::getId)
            .collect(Collectors.toList());
    }

    private ListaPaginada<Patrimonio> buscar(Patrimonio.Filtro filtro) {
        ListaPaginada<Patrimonio> patrimonios = patrimonioDataProvider.buscarPorFiltro(filtro);
        List<Patrimonio> patrimonioList = new ArrayList<>();

        if(filtroVazio(filtro)){
            patrimonios.setItems(patrimonioList);
        }
        return patrimonios;
    }

    private Boolean filtroVazio( Patrimonio.Filtro filtro){
        return Objects.isNull(filtro.getUnidadeOrganizacionalIds()) || filtro.getUnidadeOrganizacionalIds().isEmpty();
    }
}
