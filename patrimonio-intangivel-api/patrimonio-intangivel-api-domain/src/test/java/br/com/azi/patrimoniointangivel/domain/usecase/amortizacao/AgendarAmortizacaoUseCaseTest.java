package br.com.azi.patrimoniointangivel.domain.usecase.amortizacao;

import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeConfiguracoesIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.agendarjobamortizacao.AgendarAmortizacaoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.agendarjobamortizacao.AgendarAmortizacaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.agendarjobamortizacao.converter.AgendarAmortizacaoOutputDataConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class AgendarAmortizacaoUseCaseTest {


    private final static LocalDate LOCAL_DATE = LocalDate.of(2020, 5, 1);
    private Clock fixedClock;

    @Before
    public void start() {
        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
    }

    @Test
    public void deveAgendarAmortizacao() {

        String dataMensalAmortizacao = "01";
        List<String> feriadosNacionais = new ArrayList<>();
        List<String> feriadosLocais = new ArrayList<>();

        feriadosNacionais.add("01/01");
        feriadosNacionais.add("21/04");
        feriadosNacionais.add("01/05");
        feriadosNacionais.add("07/09");

        feriadosLocais.add("04/05");
        feriadosLocais.add("26/08");

        AgendarAmortizacaoUseCaseImpl usecase = new AgendarAmortizacaoUseCaseImpl(
            Mockito.mock(SistemaDeConfiguracoesIntegration.class),
            new AgendarAmortizacaoOutputDataConverter(),
            dataMensalAmortizacao,
            feriadosNacionais,
            feriadosLocais,
            fixedClock
        );

        AgendarAmortizacaoOutputData outputData = usecase.executar();
        Assert.assertEquals(LocalDate.of(2020, 5, 1), outputData.getData());
        Assert.assertEquals("0 5 0 1 5 ?", outputData.getCron());

    }
}
