package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.parametro.buscar;

import br.com.azi.patrimoniointangivel.domain.entity.PropriedadesProjeto;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.parametro.buscar.converter.BuscarParametrosOutputDataConverter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BuscarParametrosUseCaseImpl implements BuscarParametrosUseCase {

    private PropriedadesProjeto propriedades;

    private BuscarParametrosOutputDataConverter converter;

    @Override
    public BuscarParametrosOutputData executar() {
        return converter.to(propriedades);
    }
}
