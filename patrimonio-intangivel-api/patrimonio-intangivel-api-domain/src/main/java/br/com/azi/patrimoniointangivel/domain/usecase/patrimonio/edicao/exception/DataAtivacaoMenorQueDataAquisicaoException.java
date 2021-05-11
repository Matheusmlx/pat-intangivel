package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.edicao.exception;

public class DataAtivacaoMenorQueDataAquisicaoException extends RuntimeException {

    public DataAtivacaoMenorQueDataAquisicaoException() {
        super("A data de ativação não pode ser menor que data de aquisição");
    }
}
