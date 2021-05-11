package br.com.azi.patrimoniointangivel.domain.usecase.arquivo.upload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadTemporarioInputData {
    private String nome;
    private String contentType;
    private byte[] content;
}
