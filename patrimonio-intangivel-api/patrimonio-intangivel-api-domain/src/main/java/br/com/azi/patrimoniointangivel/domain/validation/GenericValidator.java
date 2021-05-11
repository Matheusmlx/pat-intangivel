package br.com.azi.patrimoniointangivel.domain.validation;

public interface GenericValidator<U> {
    void validate(U u);
}
