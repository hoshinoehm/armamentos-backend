package com.example.auth.domain.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class ControleArmamentoProcessoVisualizacaoDTO {
    // Militar
    private String nomeCompleto;
    private String nomeGuerra;
    private String matricula;
    private String rgMilitar;
    private String cpf;
    private String postoGraduacao;
    private String quadro;
    private String dataNascimento;
    private String subUnidade;
    private String cidade;
    private String estado;
    private String email;
    private String telefone;
    private String enderecoCompleto;
    private String identificacaoCompleta;

    // Arma
    private String especie;
    private String marca;
    private String modelo;
    private String calibre;
    private String numeroSerie;
    private String numeroTiro;
    private String cano;
    private String estadoConservacao;
    private String tombo;
    private Integer quantidadeCarregadores;

    // Militar (campos adicionais)
    private String dataInclusao;
    private String comportamento;
    private String situacaoJuridica;

    // Controle
    private Integer quantidadeMunicao;
    private String dataEntrega;
    private String observacoes;

    public void setEnderecoCompleto(String logradouro, String numero, String bairro, String cidade, String estado) {
        this.enderecoCompleto = String.format("%s, %s, %s, %s - %s", logradouro, numero, bairro, cidade, estado);
    }

    public void setDataEntrega(LocalDate data) {
        if (data != null) {
            this.dataEntrega = data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
    }
}
