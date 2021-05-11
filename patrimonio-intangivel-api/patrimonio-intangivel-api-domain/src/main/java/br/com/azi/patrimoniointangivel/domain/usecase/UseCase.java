package br.com.azi.patrimoniointangivel.domain.usecase;

public interface UseCase<INPUT, OUTPUT> {
    OUTPUT executar(INPUT inputData);
}
