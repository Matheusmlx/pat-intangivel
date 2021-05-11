package br.com.azi.patrimoniointangivel.domain.constant.permissoes;

public enum PermissaoPatrimonioConstants {
    NORMAL("Patrimonio.Normal"), CONSULTA("Patrimonio.Consulta");
    private final String description;

    PermissaoPatrimonioConstants(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
