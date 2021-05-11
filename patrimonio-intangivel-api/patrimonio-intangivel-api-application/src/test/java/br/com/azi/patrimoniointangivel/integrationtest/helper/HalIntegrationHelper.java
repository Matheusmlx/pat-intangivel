package br.com.azi.patrimoniointangivel.integrationtest.helper;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class HalIntegrationHelper {

    public static void createMockRequisicaoHalSucesso(WireMockRule wireMockRule, String file, String url) {
        String response = FileHelper.getJson("setup", file);

        wireMockRule.stubFor(WireMock.get(urlEqualTo(url))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(response)));
    }
}
