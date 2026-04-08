package com.example.auth.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArmaDTO {
    private Long id;
    private String numeroSerie;
    private String tipo;
    private String marca;
    private String modelo;
    private String calibre;
    private String acabamento;
    private String numeroTiro;
    private String cano;
    private String numeroPMMA;
    private String tombo;
    private String estadoConservacao;
    private Integer qtdeCarregador;
    private String maleta;
    private String acessorios;
    private String procedencia;
    private String obs;
}