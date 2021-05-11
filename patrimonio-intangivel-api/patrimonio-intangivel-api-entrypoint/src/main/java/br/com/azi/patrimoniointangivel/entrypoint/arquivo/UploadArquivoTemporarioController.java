package br.com.azi.patrimoniointangivel.entrypoint.arquivo;

import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.upload.UploadTemporarioInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.upload.UploadTemporarioOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.upload.UploadTemporarioUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v1/arquivos")
public class UploadArquivoTemporarioController {

    @Autowired
    private UploadTemporarioUseCase usecase;

    @PostMapping(consumes = "multipart/form-data")
    public UploadTemporarioOutputData executar(@RequestParam("file") MultipartFile file) throws IOException {
        UploadTemporarioInputData inputData = UploadTemporarioInputData
            .builder()
            .nome(file.getOriginalFilename())
            .contentType(file.getContentType())
            .content(file.getBytes())
            .build();
        return usecase.executar(inputData);
    }
}
