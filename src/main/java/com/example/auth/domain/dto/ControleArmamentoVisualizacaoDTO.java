package com.example.auth.domain.dto;


import com.example.auth.domain.ControleArmamento;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ControleArmamentoVisualizacaoDTO {
    private Long id;
    private Long militarId;
    private Long armaId;
    private Long coleteId;
    private String militarNome;       // NOVO
    private String armaNumeroSerie;   // NOVO
    private Integer quantidadeMunicao;
    private LocalDate dataEntrega;
    private LocalDate dataDevolucao;
    private String observacoes;

    public static ControleArmamentoVisualizacaoDTO fromEntity(ControleArmamento entity) {
        ControleArmamentoVisualizacaoDTO dto = new ControleArmamentoVisualizacaoDTO();
        dto.setId(entity.getId());
        dto.setMilitarId(entity.getMilitar() != null ? entity.getMilitar().getId() : null);
        dto.setMilitarNome(entity.getMilitar() != null ? entity.getMilitar().getNomeCompleto() : null);
        dto.setArmaId(entity.getArma() != null ? entity.getArma().getId() : null);
        dto.setArmaNumeroSerie(entity.getArma() != null ? entity.getArma().getNumeroSerie() : null);
        dto.setColeteId(entity.getColete() != null ? entity.getColete().getId() : null);
        dto.setQuantidadeMunicao(entity.getQuantidadeMunicao());
        dto.setDataEntrega(entity.getDataEntrega());
        dto.setDataDevolucao(entity.getDataDevolucao());
        dto.setObservacoes(entity.getObservacoes());
        return dto;
    }
}
