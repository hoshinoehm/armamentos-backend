package com.example.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "estado")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estado implements Serializable {

    @Id
    private Integer id;

    @Column(length = 75)
    private String nome;

    @Column(length = 2)
    private String uf;

    @Column
    private Integer ibge;

    @Column
    private Integer pais;

    @Column(length = 50)
    private String ddd;


    @OneToMany(mappedBy = "estado")
    @JsonIgnore // <- EVITA o ciclo
    private List<Cidade> cidade;
}