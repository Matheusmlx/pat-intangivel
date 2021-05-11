package br.com.azi.patrimoniointangivel.configuration.factory.usecase.movimentacao;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.edicao.EditarMovimentacaoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.edicao.EditarMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.edicao.conveter.EditarMovimentacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class EditarMovimentacaoFactory {
    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    @Autowired
    private EditarMovimentacaoOutputDataConverter outputDataConverter;

    @Bean("EditarMovimentacaoUseCase")
    @DependsOn({"EditarMovimentacaoOutputDataConverter"})
    public EditarMovimentacaoUseCase createUseCase(EditarMovimentacaoOutputDataConverter outputDataConverter){
        return new EditarMovimentacaoUseCaseImpl(movimentacaoDataProvider,notaLancamentoContabilDataProvider,outputDataConverter);
    }

    @Bean("EditarMovimentacaoOutputDataConverter")
    public EditarMovimentacaoOutputDataConverter createConverter(){
        return new EditarMovimentacaoOutputDataConverter();
    }
}
