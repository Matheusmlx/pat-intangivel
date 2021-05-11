package br.com.azi.patrimoniointangivel.configuration.factory.usecase.documentosmovimentacao;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.deletar.DeletarDocumentoMovimentacaoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.deletar.DeletarDocumentoMovimentacaoUseCaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeletarDocumentoMovimentacaoFactory {

    @Autowired
    private DocumentoDataProvider documentoDataProvider;

    @Bean
    public DeletarDocumentoMovimentacaoUseCase deletarDocumentoMovimentacaoUseCase() {
        return new DeletarDocumentoMovimentacaoUseCaseImpl(documentoDataProvider);
    }
}
