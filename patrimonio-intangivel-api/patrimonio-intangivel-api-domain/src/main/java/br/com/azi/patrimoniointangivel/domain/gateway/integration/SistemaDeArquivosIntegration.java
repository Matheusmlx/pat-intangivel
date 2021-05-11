package br.com.azi.patrimoniointangivel.domain.gateway.integration;

import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;

public interface SistemaDeArquivosIntegration {

    Arquivo upload(Arquivo arquivo);

    Arquivo download(String fileUri);

    void promote(Arquivo arquivo);

    void removeDefinitiveFile(Arquivo arquivo);

    Arquivo getMetadata(String fileUri);

}
