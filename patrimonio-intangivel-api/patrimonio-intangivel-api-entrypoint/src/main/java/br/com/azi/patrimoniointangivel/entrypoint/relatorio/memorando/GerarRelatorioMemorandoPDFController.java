package br.com.azi.patrimoniointangivel.entrypoint.relatorio.memorando;

import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.gerarrelatorio.GerarRelatorioMemorandoPDFInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.gerarrelatorio.GerarRelatorioMemorandoPDFOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.gerarrelatorio.GerarRelatorioMemorandoPDFUseCase;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/relatorios/memorando/pdf/{patrimonioId}")
public class GerarRelatorioMemorandoPDFController {

    @Autowired
    private GerarRelatorioMemorandoPDFUseCase usecase;

    @GetMapping
    public void executar(GerarRelatorioMemorandoPDFInputData inputData, HttpServletResponse httpServletResponse) throws IOException{
        GerarRelatorioMemorandoPDFOutputData outputData = usecase.executar(inputData);

        prepareDownloadResponse(outputData, httpServletResponse);
    }

    private void prepareDownloadResponse(GerarRelatorioMemorandoPDFOutputData arquivo, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType(arquivo.getContentType());
        httpServletResponse.setHeader("content-disposition", "attachment; filename=\"" + arquivo.getNome() + "\"");

        ByteArrayInputStream byt = new ByteArrayInputStream(arquivo.getContent());

        IOUtils.copy(byt, httpServletResponse.getOutputStream());
        httpServletResponse.flushBuffer();
    }
}
