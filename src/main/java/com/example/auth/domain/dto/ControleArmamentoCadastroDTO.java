package com.example.auth.domain.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ControleArmamentoCadastroDTO {
    private Long militarId;
    private Long armaId;
    private Integer quantidadeMunicao;
    private LocalDate dataEntrega;
    private String observacoes;
}
