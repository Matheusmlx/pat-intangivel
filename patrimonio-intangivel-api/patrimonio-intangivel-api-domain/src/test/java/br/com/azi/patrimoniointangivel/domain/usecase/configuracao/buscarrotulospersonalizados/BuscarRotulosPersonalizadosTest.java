package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.buscarrotulospersonalizados;

import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.rotulospersonalizados.BuscarRotulosPersonalizadosOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.rotulospersonalizados.BuscarRotulosPersonalizadosUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.rotulospersonalizados.BuscarRotulosPersonalizadosUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.rotulospersonalizados.converter.RotulosPersonalizadosConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class BuscarRotulosPersonalizadosTest {

    private final Map<String, Object> rotulosPersonalizados = Map.of(
        "i18n", new Object[]{
            "CONTAS_CONTABEIS", Map.of("campos", Map.of("numero", "Número")),
            "DASHBOARD", Map.of("campos", Map.of("patrimonio", "Patrimônio"))
        });

    private BuscarRotulosPersonalizadosUseCase useCase;

    @Test
    public void deveRetornarOsRotulosPersonalizados() throws NoSuchMethodException {
        useCase = new BuscarRotulosPersonalizadosUseCaseImpl(
            rotulosPersonalizados,
            new RotulosPersonalizadosConverter()
        );

        final BuscarRotulosPersonalizadosOutputData outputData = useCase.executar();

        assertNotNull(outputData.getRotulosPersonalizados());
        assertEquals(outputData.getRotulosPersonalizados(), rotulosPersonalizados);
    }
}
