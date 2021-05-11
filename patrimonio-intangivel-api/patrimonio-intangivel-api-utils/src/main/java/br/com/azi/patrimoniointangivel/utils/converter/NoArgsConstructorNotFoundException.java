package br.com.azi.patrimoniointangivel.utils.converter;

public class NoArgsConstructorNotFoundException extends RuntimeException {
    public NoArgsConstructorNotFoundException() {
        super("No arguments constructor is required.");
    }
}
