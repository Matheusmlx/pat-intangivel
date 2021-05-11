package br.com.azi.patrimoniointangivel.entrypoint.arquivo;

import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.download.DownloadInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.download.DownloadOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.download.DownloadUseCase;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/v1/arquivos")
public class DownloadArquivoController {

    @Autowired
    private DownloadUseCase usecase;

    @GetMapping
    public void executar(DownloadInputData inputData, HttpServletResponse httpServletResponse) throws IOException {
        DownloadOutputData outputData = usecase.executar(inputData);
        prepareDownloadResponse(outputData, httpServletResponse);
    }

    private void prepareDownloadResponse(DownloadOutputData arquivo, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType(arquivo.getContentType());
        httpServletResponse.setHeader("content-disposition", "attachment; filename=\"" + arquivo.getNome() + "\"");

        ByteArrayInputStream byt = new ByteArrayInputStream(arquivo.getContent());

        IOUtils.copy(byt, httpServletResponse.getOutputStream());
        httpServletResponse.flushBuffer();
    }
}
