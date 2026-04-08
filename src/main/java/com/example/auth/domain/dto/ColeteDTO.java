package com.example.auth.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColeteDTO {
    private Long id;
    private String marca;
    private String modelo;
    private String tamanho;
    private LocalDate validade;
    private Integer anoFabricacao;
    private String nivelProtecao;
    private String genero;
    private String numeroSerie;
    private String numeroGuia;
    private String tombo;
    private String conservacao;
    private String situacao;
    private String obs;
    private String local;
    private Boolean cautelaIndividual;
    private Integer livroPagina;
    private Integer livroOrdem;
}