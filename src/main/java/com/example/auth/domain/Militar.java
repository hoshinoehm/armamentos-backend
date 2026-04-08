package com.example.auth.domain;


import com.example.auth.domain.enums.PostoGraduacao;
import com.example.auth.domain.enums.Quadro;
import com.example.auth.domain.enums.Sexo;
import com.example.auth.domain.enums.Situacao;
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
@Table(name = "militares")
public class Militar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeCompleto;
    private String nomeGuerra;
    private String matricula;
    private String cpf;
    private String rgMilitar;
    private String celular;

    @ManyToOne
    @JoinColumn(name = "cidade") // <-- deve ser "cidade"
    private Cidade cidade;

    @ManyToOne
    @JoinColumn(name = "estado") // <-- deve ser "estado"
    private Estado estado;


    @Column(name = "id_servidor")
    private Long idServidor;

    @ManyToOne
    @JoinColumn(name = "sub_unidade_id")
    private SubUnidade subUnidade;

    @Enumerated(EnumType.STRING)
    private Situacao situacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "posto_graduacao")
    private PostoGraduacao postoGraduacao;

    @Enumerated(EnumType.STRING)
    private Quadro quadro;

    private String numeroBarra;

    @Column(name = "data_inclusao")
    private LocalDate dataInclusao;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @ManyToOne
    @JoinColumn(name = "funcao_id")
    private Funcao funcao;

    @Column(name = "serve_31bpm")
    private String serve31bpm;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private String email;
    private String logradouro;
    private String numeroCasa;
    private String bairro;
    private String enderecoCompleto;
}
