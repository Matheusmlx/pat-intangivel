package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.listagempatrimonio.relatorioxls;

import br.com.azi.patrimoniointangivel.domain.constant.permissoes.PermissaoPatrimonioConstants;
import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;
import br.com.azi.patrimoniointangivel.domain.entity.ListaPaginada;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarlistagem.exception.FiltroIncompletoException;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.listagempatrimonio.relatorioxls.converter.GerarRelatorioListagemPatrimonioConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.listagempatrimonio.relatorioxls.converter.GerarRelatorioListagemPatrimonioXLSOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
public class GerarRelatorioListagemPatrimonioXLSUseCaseImpl implements GerarRelatorioListagemPatrimonioXLSUseCase {

    private PatrimonioDataProvider patrimonioDataProvider;

    private SistemaDeRelatoriosIntegration sistemaDeRelatoriosIntegration;

    private GerarRelatorioListagemPatrimonioConverter gerarRelatorioListagemPatrimonioConverter;

    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    private GerarRelatorioListagemPatrimonioXLSOutputDataConverter outputDataConverter;

    @Override
    public GerarRelatorioListagemPatrimonioXLSOutputData executar(GerarRelatorioListagemPatrimonioXLSInputData inputData) {
        validarDadosEntrada(inputData);

        Patrimonio.Filtro filtro = criarFiltro(inputData);

        ListaPaginada<Patrimonio> entidadesEncontradas = buscar(filtro);

        Arquivo arquivo = sistemaDeRelatoriosIntegration.gerarRelatorioListagemPatrimoniosXLS(entidadesEncontradas);
        return outputDataConverter.to(arquivo);
    }

    private void validarDadosEntrada(GerarRelatorioListagemPatrimonioXLSInputData entrada){
        Validator.of(entrada)
            .validate(GerarRelatorioListagemPatrimonioXLSInputData::getSize, size -> Objects.nonNull(size) && size > 0, new FiltroIncompletoException("Ausência da quantidade de registros por página."))
            .validate(GerarRelatorioListagemPatrimonioXLSInputData::getPage, Objects::nonNull, new FiltroIncompletoException("Ausência do número da página."))
            .get();
    }

    private Patrimonio.Filtro criarFiltro(GerarRelatorioListagemPatrimonioXLSInputData inputData){
        Patrimonio.Filtro filtro = gerarRelatorioListagemPatrimonioConverter.to(inputData);
        filtro.setUnidadeOrganizacionalIds(buscarOrgaosIdsAcessoUsuario());
        return filtro;
    }

    private List<Long> buscarOrgaosIdsAcessoUsuario(){
        List<String> funcoes = new ArrayList<>();
        funcoes.add(PermissaoPatrimonioConstants.NORMAL.getDescription());
        funcoes.add(PermissaoPatrimonioConstants.CONSULTA.getDescription());

        List<UnidadeOrganizacional> unidadeOrganizacionals =  sistemaDeGestaoAdministrativaIntegration.buscarUnidadesOrganizacionaisPorFuncao(funcoes);

        return unidadeOrganizacionals
            .stream()
            .map(UnidadeOrganizacional::getId)
            .collect(Collectors.toList());
    }

    private ListaPaginada<Patrimonio> buscar(Patrimonio.Filtro filtro){
        ListaPaginada<Patrimonio> patrimonios = patrimonioDataProvider.buscarPorFiltro(filtro);
        List<Patrimonio> patrimonioList = new ArrayList<>();

        if(filtroVazio(filtro)){
            patrimonios.setItems(patrimonioList);
        }
        return patrimonios;
    }

    private boolean filtroVazio(Patrimonio.Filtro filtro){
        return Objects.isNull(filtro.getUnidadeOrganizacionalIds()) || filtro.getUnidadeOrganizacionalIds().isEmpty();
    }
}
