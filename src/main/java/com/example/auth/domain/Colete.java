package com.example.auth.domain;


import com.example.auth.domain.enums.Sexo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Colete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;
    private String modelo;
    private String tamanho;
    private LocalDate validade;
    private Date anoFabricacao;
    private String nivelProtecao;
    @Enumerated(EnumType.STRING)
    private Sexo genero;
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