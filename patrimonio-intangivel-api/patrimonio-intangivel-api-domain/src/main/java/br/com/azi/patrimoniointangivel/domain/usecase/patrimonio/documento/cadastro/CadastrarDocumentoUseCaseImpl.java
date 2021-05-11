package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.cadastro;

import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;
import br.com.azi.patrimoniointangivel.domain.entity.Documento;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.TipoDocumento;
import br.com.azi.patrimoniointangivel.domain.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.exception.TipoDocumentoNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.TipoDocumentoDataprovider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.cadastro.converter.CadastrarDocumentoOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.cadastro.expection.NumeroUnicoException;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.cadastro.expection.QuantidadeDeDocumentosCadastradosExcedidoException;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class CadastrarDocumentoUseCaseImpl implements CadastrarDocumentoUseCase {

    private PatrimonioDataProvider patrimonioDataProvider;

    private TipoDocumentoDataprovider tipoDocumentoDataprovider;

    private DocumentoDataProvider documentoDataProvider;

    private CadastrarDocumentoOutputDataConverter outputDataConverter;

    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Override
    public CadastrarDocumentoOutputData executar(CadastrarDocumentoInputData input) {
        validarEntrada(input);
        validarSeNumeroDocumentoUnico(input);

        Documento documento = criarEntidade(input);
        validaQuantidadeDeDocumentos(input);
        Documento documentoSalvo = salvar(documento);
        promoverArquivoTemporario(documentoSalvo);
        return outputDataConverter.to(documentoSalvo);
    }

    private void validarEntrada(CadastrarDocumentoInputData inputData) {
        Validator.of(inputData)
            .validate(CadastrarDocumentoInputData::getIdPatrimonio, Objects::nonNull, "Não há documentos em anexo")
            .get();
    }

    private void validarSeNumeroDocumentoUnico(CadastrarDocumentoInputData inputData) {
        if (documentoDataProvider.existeDocumentoComNumero(inputData.getIdPatrimonio(), inputData.getNumero(), inputData.getIdTipoDocumento())) {
            throw new NumeroUnicoException();
        }
    }

    private Documento criarEntidade(CadastrarDocumentoInputData input) {
        Patrimonio entidadeEncontrada = buscarPatrimonio(input);
        TipoDocumento tipoDocumentoEncontrado = buscarTipoDocumento(input);

        LocalDateTime data = null;
        if(Objects.nonNull(input.getData())){
            data = LocalDateTime.ofInstant(input.getData().toInstant(),ZoneOffset.UTC);
        }
        return Documento
            .builder()
            .numero(input.getNumero())
            .patrimonio(entidadeEncontrada)
            .tipoDocumento(tipoDocumentoEncontrado)
            .data(data)
            .uriAnexo(input.getUriAnexo())
            .valor(input.getValor())
            .build();
    }

    private Documento salvar(Documento documento) {
        return documentoDataProvider.salvar(documento);
    }

    public void validaQuantidadeDeDocumentos(CadastrarDocumentoInputData inputData){
        Long quantidade = documentoDataProvider.qntDocumentosPorPatrimonioId(inputData.getIdPatrimonio());
        if(quantidade >= 30){
            throw new QuantidadeDeDocumentosCadastradosExcedidoException();
        }
    }

    private void promoverArquivoTemporario(Documento documento) {
        Arquivo arquivo = Arquivo.builder()
            .uri(documento.getUriAnexo())
            .build();
        sistemaDeArquivosIntegration.promote(arquivo);
    }

    private Patrimonio buscarPatrimonio(CadastrarDocumentoInputData input) {
        Optional<Patrimonio> entidade = patrimonioDataProvider.buscarPorId(input.getIdPatrimonio());
        return entidade.orElseThrow(PatrimonioNaoEncontradoException::new);
    }

    private TipoDocumento buscarTipoDocumento(CadastrarDocumentoInputData input) {
        Optional<TipoDocumento> entidade = tipoDocumentoDataprovider.buscarPorId(input.getIdTipoDocumento());
        return entidade.orElseThrow(TipoDocumentoNaoEncontradoException::new);
    }
}
