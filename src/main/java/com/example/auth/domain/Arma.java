package com.example.auth.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Arma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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