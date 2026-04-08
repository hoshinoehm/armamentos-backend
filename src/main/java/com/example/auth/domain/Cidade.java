package com.example.auth.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "cidade")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cidade implements Serializable {

    @Id
    private Integer id;

    @Column(length = 120)
    private String nome;

    @Column
    private Integer ibge;

    @ManyToOne
    @JoinColumn(name = "uf")
    private Estado estado;
}