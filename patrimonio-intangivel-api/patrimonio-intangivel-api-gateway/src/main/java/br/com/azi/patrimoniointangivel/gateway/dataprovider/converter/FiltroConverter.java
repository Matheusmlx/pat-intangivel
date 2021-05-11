package br.com.azi.patrimoniointangivel.gateway.dataprovider.converter;

import br.com.azi.patrimoniointangivel.domain.entity.FiltroBase;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FiltroConverter {

    private FiltroConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static Pageable extrairPaginacao(FiltroBase filtro) {
        return PageRequest.of(
            filtro.getPage().intValue(), filtro.getSize().intValue(),
            new Sort(Sort.Direction.valueOf(filtro.getDirection()), Objects.nonNull(filtro.getSort()) ? filtro.getSort() : "id")
        );
    }

    public static Pageable extrairPaginacao(FiltroBase filtro, String sortFirst, String sortSecond) {
        List<String> sort = new ArrayList<>();
        sort.add(sortFirst);
        sort.add(sortSecond);

        return PageRequest.of(
            filtro.getPage().intValue(), filtro.getSize().intValue(),
            new Sort(Sort.Direction.valueOf(filtro.getDirection()), sort)
        );
    }
}
