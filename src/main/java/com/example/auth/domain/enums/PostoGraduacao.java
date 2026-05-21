package com.example.auth.domain.enums;

public enum PostoGraduacao {

    CORONEL("CEL"),
    TENENTE_CORONEL("TC"),
    MAJOR("MAJ"),
    CAPITAO("CAP"),
    PRIMEIRO_TENENTE("1º TEN"),
    SEGUNDO_TENENTE("2º TEN"),
    ASPIRANTE("ASP OF"),
    CAD_PM("CAD PM"),
    SUBTENENTE("SUBTEN"),
    PRIMEIRO_SARGENTO("1º SGT"),
    SEGUNDO_SARGENTO("2º SGT"),
    TERCEIRO_SARGENTO("3º SGT"),
    CABO("CB"),
    SOLDADO("SD");

    private final String label;

    PostoGraduacao(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
