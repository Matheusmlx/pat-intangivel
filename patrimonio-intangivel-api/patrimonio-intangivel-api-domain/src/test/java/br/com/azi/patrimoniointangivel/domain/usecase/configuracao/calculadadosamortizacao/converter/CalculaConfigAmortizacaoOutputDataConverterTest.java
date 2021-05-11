package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.calculadadosamortizacao.converter;

import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.calculadadosamortizacao.CalculaConfigAmortizacaoOutputData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CalculaConfigAmortizacaoOutputDataConverterTest{

    @InjectMocks
    CalculaConfigAmortizacaoOutputDataConverter calculaConfigAmortizacaoOutputDataConverter;

    @Test(expected = UnsupportedOperationException.class)
    public void returnUnsupportedOperationExceptionWhenCallFrom() {
        calculaConfigAmortizacaoOutputDataConverter.from(new CalculaConfigAmortizacaoOutputData());
    }
}
