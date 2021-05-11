package br.com.azi.patrimoniointangivel.gateway.dataprovider;

import br.com.azi.patrimoniointangivel.domain.entity.Documento;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.converter.DocumentoConverter;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.DocumentoEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.repository.DocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DocumentoDataProviderImpl implements DocumentoDataProvider {

    @Autowired
    private DocumentoRepository repository;

    @Autowired
    private DocumentoConverter converter;

    @Override
    public Optional<Documento> buscarPorId(Long id) {
        Optional<DocumentoEntity> encontrada = repository.findById(id);
        return encontrada.map(documentoEntity -> converter.to(documentoEntity));
    }

    @Override
    public List<Documento> buscarDocumentoPorPatrimonioId(Long id) {
        List<DocumentoEntity> documentos = repository.findAllByPatrimonio_Id(id);

        return documentos
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());
    }

    @Override
    public List<Documento> buscarDocumentoPorMovimentacaoId(Long idMovimentacao) {
        List<DocumentoEntity> documentos = repository.findAllByMovimentacao_Id(idMovimentacao);

        return documentos
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());
    }

    @Override
    public Documento salvar(Documento business) {
        DocumentoEntity salva = repository.save(converter.from(business));
        return converter.to(salva);
    }

    @Override
    @Transactional
    public Documento atualizar(Documento business) {
        DocumentoEntity salvo = repository.save(converter.from(business));
        return converter.to(salvo);
    }

    @Override
    public boolean existeDocumentoComNumero(Long idPatrimonio, String numero, Long tipo) {
        return repository.existsByPatrimonio_IdAndNumeroAndTipoDocumento_Id(idPatrimonio, numero, tipo);
    }

    @Override
    public boolean existeDocumentoMovimentacaoComNumero(Long idMovimentacao, String numero, Long tipo) {
        return repository.existsByMovimentacao_IdAndNumeroAndTipoDocumento_Id(idMovimentacao, numero, tipo);
    }

    @Override
    public Optional<Documento> buscarDocumentoComNumero(Long idPatrimonio, String numero) {
        Optional<DocumentoEntity> encontrada = repository.findByPatrimonio_IdAndNumero(idPatrimonio, numero);
        return encontrada.map(documentoEntity -> converter.to(documentoEntity));
    }

    @Override
    @Transactional
    public void remover(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void removeDocumentosPorPatrimonioId(Long id) {
        List<Documento> documentos = buscarDocumentoPorPatrimonioId(id);
        for (Documento documento : documentos) {
            remover(documento.getId());
        }
    }

    @Override
    public Long qntDocumentosPorPatrimonioId(Long patrimonioId){
        return repository.countByPatrimonio_Id(patrimonioId);
    }

    @Override
    public Long qntDocumentosPorMovimentacaoId(Long movimentacaoId) {
        return repository.countByMovimentacao_Id(movimentacaoId);
    }
}
