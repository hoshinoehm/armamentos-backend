package com.example.auth.domain.enums;

public enum Comportamento {

    EXCEPCIONAL("Excepcional"),
    OTIMO("Ótimo"),
    BOM("Bom"),
    INSUFICIENTE("Insuficiente"),
    MAU("Mau");

    private final String label;

    Comportamento(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
