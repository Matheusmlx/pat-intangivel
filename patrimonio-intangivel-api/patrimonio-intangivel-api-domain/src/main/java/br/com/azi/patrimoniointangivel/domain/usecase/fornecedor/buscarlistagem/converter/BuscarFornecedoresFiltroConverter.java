package br.com.azi.patrimoniointangivel.domain.usecase.fornecedor.buscarlistagem.converter;

import br.com.azi.patrimoniointangivel.domain.entity.Fornecedor;
import br.com.azi.patrimoniointangivel.domain.usecase.fornecedor.buscarlistagem.BuscarFornecedoresInputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BuscarFornecedoresFiltroConverter extends GenericConverter<BuscarFornecedoresInputData, Fornecedor.Filtro> {
}
