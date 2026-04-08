package com.example.auth.domain.enums;


public enum PostoGraduacao {

    SOLDADO("Soldado"),
    CABO("Cabo"),
    TERCEIRO_SARGENTO("3° Sargento"),
    SEGUNDO_SARGENTO("2° Sargento"),
    PRIMEIRO_SARGENTO("1° Sargento"),
    SUBTENENTE("Subtenente"),
    ASPIRANTE("Aspirante"),
    SEGUNDO_TENENTE("2° Tenente"),
    PRIMEIRO_TENENTE("1° Tenente"),
    CAPITAO("Capitão"),
    MAJOR("Major"),
    TENENTE_CORONEL("Tenente-Coronel"),
    CORONEL("Coronel");

    private final String label;

    PostoGraduacao(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

