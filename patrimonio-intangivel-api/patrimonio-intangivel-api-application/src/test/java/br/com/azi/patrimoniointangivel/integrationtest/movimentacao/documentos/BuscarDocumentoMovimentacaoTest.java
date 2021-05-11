package br.com.azi.patrimoniointangivel.integrationtest.movimentacao.documentos;

import br.com.azi.patrimoniointangivel.integrationtest.helper.AuthenticationHelper;
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BuscarDocumentoMovimentacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Rollback
    @Sql({"/datasets/movimentacao/documento/buscar-documento-movimentacao.sql"})
    @Transactional
    public void deveBuscarDocumentoPorIdMovimentacao() throws Exception{
        mockMvc.perform(
            get("/movimentacao/1/documentos")
                .headers(AuthenticationHelper.getHeaders())
                .contentType(MediaType.APPLICATION_JSON)

        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items", hasSize(2)))
            .andExpect(jsonPath("$.items[0].id", is(1)))
            .andExpect(jsonPath("$.items[0].numero", is("12")))
            .andExpect(jsonPath("$.items[0].idMovimentacao", is(1)))
            .andExpect(jsonPath("$.items[0].idPatrimonio", is(1)))
            .andExpect(jsonPath("$.items[0].idTipoDocumento", is(1)))
            .andExpect(jsonPath("$.items[1].id", is(2)))
            .andExpect(jsonPath("$.items[1].numero", is("3214")))
            .andExpect(jsonPath("$.items[1].idMovimentacao", is(1)))
            .andExpect(jsonPath("$.items[1].idPatrimonio", is(1)))
            .andExpect(jsonPath("$.items[1].idTipoDocumento", is(1)))
            .andDo(print());
    }
}
