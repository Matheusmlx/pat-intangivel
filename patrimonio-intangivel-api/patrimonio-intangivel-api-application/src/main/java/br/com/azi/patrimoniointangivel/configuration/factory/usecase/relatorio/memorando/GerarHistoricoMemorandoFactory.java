package br.com.azi.patrimoniointangivel.configuration.factory.usecase.relatorio.memorando;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.HistoricoMemorandoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.upload.UploadTemporarioUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.historicomemorando.GerarHistoricoMemorandoUseCaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GerarHistoricoMemorandoFactory {

    @Autowired
    private UploadTemporarioUseCase  uploadTemporarioUseCase;

    @Autowired
    private HistoricoMemorandoDataProvider historicoMemorandoDataProvider;

    @Bean("GerarHistoricoMemorandoUseCase")
    public GerarHistoricoMemorandoUseCaseImpl createUseCase(){
        return new GerarHistoricoMemorandoUseCaseImpl(uploadTemporarioUseCase, historicoMemorandoDataProvider);
    }
}
