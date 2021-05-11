package br.com.azi.patrimoniointangivel.integrationtest.patrimonio.documento;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BuscarDocumentoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Rollback
    @Sql({"/datasets/patrimonio/documento/remover-documento.sql"})
    @Transactional
    public void deveBuscarDocumentoPorIdPatrimonio() throws  Exception{
        mockMvc.perform(
            get("/patrimonios/1/documentos")
                .contentType(MediaType.APPLICATION_JSON)

        )
            .andExpect(status().is2xxSuccessful());
    }
}
