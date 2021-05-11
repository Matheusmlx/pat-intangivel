package br.com.azi.patrimoniointangivel.entrypoint.relatorio.inventario.analitico;


import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.analitico.GerarInventarioAnaliticoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.analitico.GerarInventarioAnaliticoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.analitico.GerarInventarioAnaliticoUseCase;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping(path = "/relatorios/inventario/analitico", params = {"formato", "orgao", "mesReferencia"})
public class GerarInventarioAnaliticoController {

    @Autowired
    private GerarInventarioAnaliticoUseCase usecase;

    @GetMapping
    public void executar(@RequestParam(value = "formato") String formato,
                         @RequestParam(value = "orgao") Long orgao,
                         @RequestParam(value = "mesReferencia") String mesReferencia,
                         HttpServletResponse httpServletResponse) throws IOException {

        GerarInventarioAnaliticoInputData inputData = new GerarInventarioAnaliticoInputData(formato, orgao, mesReferencia);
        GerarInventarioAnaliticoOutputData outputData = usecase.executar(inputData);
        prepareDownloadResponse(outputData, httpServletResponse);

    }

    private void prepareDownloadResponse(GerarInventarioAnaliticoOutputData arquivo, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType(arquivo.getContentType());
        httpServletResponse.setHeader("content-disposition", "attachment; filename=\"" + arquivo.getNome() + "\"");

        ByteArrayInputStream byt = new ByteArrayInputStream(arquivo.getContent());

        IOUtils.copy(byt, httpServletResponse.getOutputStream());
        httpServletResponse.flushBuffer();
    }

}

