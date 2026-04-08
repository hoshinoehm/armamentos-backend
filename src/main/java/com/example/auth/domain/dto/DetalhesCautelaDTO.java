package com.example.auth.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DetalhesCautelaDTO {

    // Dados do militar
    private String postoGraduacao;
    private String nomeCompleto;
    private String rgMilitar;

    // Dados da arma
    private String numeroSerie;
    private String tipo;
    private String marca;
    private String modelo;
    private String calibre;
    private String numeroTiro;
    private String cano;

    // Dados da cautela
    private LocalDateTime createdAt;
}
