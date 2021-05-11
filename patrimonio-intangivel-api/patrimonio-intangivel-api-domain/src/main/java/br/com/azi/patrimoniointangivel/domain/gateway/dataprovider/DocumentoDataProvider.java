package br.com.azi.patrimoniointangivel.domain.gateway.dataprovider;

import br.com.azi.patrimoniointangivel.domain.entity.Documento;

import java.util.List;
import java.util.Optional;

public interface DocumentoDataProvider {

    Documento salvar(Documento documento);

    boolean existeDocumentoComNumero(Long idPatrimonio, String numero, Long tipo) ;

    boolean existeDocumentoMovimentacaoComNumero(Long idMovimentacao, String numero, Long tipo);

    Optional<Documento> buscarDocumentoComNumero(Long idPatrimonio, String numero);

    List<Documento> buscarDocumentoPorPatrimonioId(Long id);

    List<Documento> buscarDocumentoPorMovimentacaoId(Long id);

    Optional<Documento> buscarPorId(Long id);

    void remover(Long id);

    void removeDocumentosPorPatrimonioId(Long id);

    Documento atualizar(Documento documento);

    Long qntDocumentosPorPatrimonioId(Long patrimonioId);

    Long qntDocumentosPorMovimentacaoId(Long movimentacaoId);

}
