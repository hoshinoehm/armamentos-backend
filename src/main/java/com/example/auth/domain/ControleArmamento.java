package com.example.auth.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ControleArmamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Militar militar;

    @ManyToOne
    private Arma arma;

    @ManyToOne
    private Colete colete;

    private Integer quantidadeMunicao;
    private LocalDate dataEntrega;
    private LocalDate dataDevolucao;
    private String observacoes;
    private LocalDateTime createdAt = LocalDateTime.now();
}
