package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.cadastro;

import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;
import br.com.azi.patrimoniointangivel.domain.entity.Documento;
import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.TipoDocumento;
import br.com.azi.patrimoniointangivel.domain.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniointangivel.domain.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.exception.TipoDocumentoNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.TipoDocumentoDataprovider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.cadastro.converter.CriarDocumentoOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.cadastro.expection.NumeroUnicoException;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.cadastro.expection.QuantidadeDeDocumentosCadastradoExcedidoException;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class CriarDocumentoMovimentacaoUseCaseImpl implements CriarDocumentoMovimentacaoUseCase {

    private MovimentacaoDataProvider movimentacaoDataProvider;

    private TipoDocumentoDataprovider tipoDocumentoDataprovider;

    private DocumentoDataProvider documentoDataProvider;

    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    private PatrimonioDataProvider patrimonioDataProvider;

    private CriarDocumentoOutputDataConverter outputDataConverter;


    @Override
    public CriarDocumentoMovimentacaoOutputData executar(CriarDocumentoMovimentacaoInputData inputData) {
        validarEntrada(inputData);
        validarSeNumeroDocumentoUnico(inputData);

        Documento documento = criarEntidade(inputData);
        validaQuantidadeDeDocumentos(inputData);
        Documento documentoSalvo = salvar(documento);
        promoverArquivoTemporario(documentoSalvo);
        return outputDataConverter.to(documentoSalvo);
    }

    private void validarEntrada(CriarDocumentoMovimentacaoInputData inputData) {
        Validator.of(inputData)
            .validate(CriarDocumentoMovimentacaoInputData::getIdMovimentacao, Objects::nonNull,"Id Movimentacao é nulo")
            .validate(CriarDocumentoMovimentacaoInputData::getIdPatrimonio, Objects::nonNull,"Id Patrimonio é nulo")
            .validate(CriarDocumentoMovimentacaoInputData::getIdTipoDocumento, Objects::nonNull,"Id Tipo Documento é nulo")
            .get();
    }

    private void validarSeNumeroDocumentoUnico(CriarDocumentoMovimentacaoInputData inputData) {
        if(documentoDataProvider.existeDocumentoMovimentacaoComNumero(inputData.getIdMovimentacao(), inputData.getNumero(),inputData.getIdTipoDocumento())){
            throw new NumeroUnicoException();
        }
    }

    private Documento criarEntidade(CriarDocumentoMovimentacaoInputData inputData){
        Movimentacao entidadeEncontrada = buscarMovimentacao(inputData);
        Patrimonio patrimonio = buscarPatrimonio(inputData);
        TipoDocumento tipoDocumentoEncontrado = buscarTipoDocumento(inputData);

        LocalDateTime data = null;
        if(Objects.nonNull(inputData.getData())){
            data = LocalDateTime.ofInstant(inputData.getData().toInstant(), ZoneOffset.UTC);
        }

        return Documento
            .builder()
            .numero(inputData.getNumero())
            .patrimonio(patrimonio)
            .movimentacao(entidadeEncontrada)
            .tipoDocumento(tipoDocumentoEncontrado)
            .data(data)
            .uriAnexo(inputData.getUriAnexo())
            .valor(inputData.getValor())
            .build();
    }

    private Movimentacao buscarMovimentacao(CriarDocumentoMovimentacaoInputData inputData){
        Optional<Movimentacao> entidade = movimentacaoDataProvider.buscarPorId(inputData.getIdMovimentacao());
        return entidade.orElseThrow(MovimentacaoNaoEncontradaException::new);
    }

    private Patrimonio buscarPatrimonio(CriarDocumentoMovimentacaoInputData input) {
        Optional<Patrimonio> entidade = patrimonioDataProvider.buscarPorId(input.getIdPatrimonio());
        return entidade.orElseThrow(PatrimonioNaoEncontradoException::new);
    }

    private TipoDocumento buscarTipoDocumento(CriarDocumentoMovimentacaoInputData input) {
        Optional<TipoDocumento> entidade = tipoDocumentoDataprovider.buscarPorId(input.getIdTipoDocumento());
        return entidade.orElseThrow(TipoDocumentoNaoEncontradoException::new);
    }

    public void validaQuantidadeDeDocumentos(CriarDocumentoMovimentacaoInputData inputData){
        Long quantidade = documentoDataProvider.qntDocumentosPorMovimentacaoId(inputData.getIdMovimentacao());
        if(quantidade >= 30){
            throw new QuantidadeDeDocumentosCadastradoExcedidoException();
        }
    }

    private Documento salvar(Documento documento) {
        return documentoDataProvider.salvar(documento);
    }

    private void promoverArquivoTemporario(Documento documento) {
        Arquivo arquivo = Arquivo.builder()
            .uri(documento.getUriAnexo())
            .build();
        sistemaDeArquivosIntegration.promote(arquivo);
    }
}
