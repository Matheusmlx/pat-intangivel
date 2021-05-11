package br.com.azi.patrimoniointangivel.entrypoint.relatorio.listagempatrimonios;

import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.listagempatrimonio.relatorioxls.GerarRelatorioListagemPatrimonioXLSInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.listagempatrimonio.relatorioxls.GerarRelatorioListagemPatrimonioXLSOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.listagempatrimonio.relatorioxls.GerarRelatorioListagemPatrimonioXLSUseCase;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Transactional
@RestController
@RequestMapping("/relatorios/listagemPatrimonio/xls")
public class GerarRelatorioListagemPatrimonioController {

    @Autowired
    private GerarRelatorioListagemPatrimonioXLSUseCase useCase;

    @GetMapping
    public void executar(GerarRelatorioListagemPatrimonioXLSInputData inputData, HttpServletResponse httpServletResponse) throws IOException{
        GerarRelatorioListagemPatrimonioXLSOutputData outputDate = useCase.executar(inputData);
        prepareDownloadResponse(outputDate,httpServletResponse);
    }

    private void prepareDownloadResponse(GerarRelatorioListagemPatrimonioXLSOutputData arquivo, HttpServletResponse httpServletResponse)  throws IOException{
        httpServletResponse.setContentType(arquivo.getContentType());
        httpServletResponse.setHeader("content-disposition", "attachment; filename=\"" + arquivo.getNome() + "\"");

        ByteArrayInputStream byt = new ByteArrayInputStream(arquivo.getContent());
        IOUtils.copy(byt, httpServletResponse.getOutputStream());
        httpServletResponse.flushBuffer();
    }
}
