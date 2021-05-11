package br.com.azi.patrimoniointangivel.gateway.integration.hal.usecase.uploadfile.converter;

import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;
import br.com.azi.patrimoniointangivel.gateway.integration.hal.usecase.uploadfile.entity.UploadFileIntegrationResponse;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import org.springframework.stereotype.Component;

@Component
public class UploadFileIntegrationConverter extends GenericConverter<UploadFileIntegrationResponse, Arquivo> {

    @Override
    public Arquivo to(UploadFileIntegrationResponse response) {
        Arquivo destino = super.to(response);

        destino.setNome(response.getName());

        return destino;
    }
}
