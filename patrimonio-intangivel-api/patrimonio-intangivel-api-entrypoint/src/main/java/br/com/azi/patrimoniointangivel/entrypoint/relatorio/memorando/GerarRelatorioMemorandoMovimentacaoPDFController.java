package br.com.azi.patrimoniointangivel.entrypoint.relatorio.memorando;

import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.gerarrelatoriomovimentacao.GerarRelatorioMemorandoMovimentacaoPDFInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.gerarrelatoriomovimentacao.GerarRelatorioMemorandoMovimentacaoPDFOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.gerarrelatoriomovimentacao.GerarRelatorioMemorandoMovimentacaoPDFUseCase;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("relatorios/memorando/movimentacao/pdf/{id}")
public class GerarRelatorioMemorandoMovimentacaoPDFController {

    @Autowired
    private GerarRelatorioMemorandoMovimentacaoPDFUseCase useCase;

    @GetMapping
    public void executar(GerarRelatorioMemorandoMovimentacaoPDFInputData inputData, HttpServletResponse httpServletResponse) throws IOException{
        GerarRelatorioMemorandoMovimentacaoPDFOutputData outputData = useCase.executar(inputData);

        prepareDownloadResponse(outputData,httpServletResponse);
    }

    private void prepareDownloadResponse(GerarRelatorioMemorandoMovimentacaoPDFOutputData arquivo, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType(arquivo.getContentType());
        httpServletResponse.setHeader("content-disposition", "attachment; filename=\"" + arquivo.getNome() + "\"");

        ByteArrayInputStream byt = new ByteArrayInputStream(arquivo.getContent());

        IOUtils.copy(byt, httpServletResponse.getOutputStream());
        httpServletResponse.flushBuffer();
    }
}
