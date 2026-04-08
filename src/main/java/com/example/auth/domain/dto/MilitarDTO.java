package com.example.auth.domain.dto;


import com.example.auth.domain.*;
import com.example.auth.domain.enums.PostoGraduacao;
import com.example.auth.domain.enums.Quadro;
import com.example.auth.domain.enums.Sexo;
import com.example.auth.domain.enums.Situacao;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MilitarDTO {

    private Long id;
    private String nomeCompleto;
    private String nomeGuerra;
    private String matricula;
    private String cpf;
    private String rgMilitar;
    private String celular;

    private Integer cidadeId;
    private Integer estadoId;

    private Long idServidor;
    private Long subUnidadeId;
    private Situacao situacao;
    private String postoGraduacao;
    private String postoGraduacaoEnum;
    private Quadro quadro;
    private String numeroBarra;
    private LocalDate dataInclusao;
    private LocalDate dataNascimento;
    private Long funcaoId;
    private String serve31bpm;
    private Sexo sexo;
    private LocalDateTime createdAt;

    private String email;
    private String logradouro;
    private String numeroCasa;
    private String bairro;

    public static MilitarDTO fromEntity(Militar militar) {
        MilitarDTO dto = new MilitarDTO();
        dto.setId(militar.getId());
        dto.setNomeCompleto(militar.getNomeCompleto());
        dto.setNomeGuerra(militar.getNomeGuerra());
        dto.setMatricula(militar.getMatricula());
        dto.setCpf(militar.getCpf());
        dto.setRgMilitar(militar.getRgMilitar());
        dto.setCelular(militar.getCelular());

        dto.setCidadeId(militar.getCidade() != null ? militar.getCidade().getId() : null);
        dto.setEstadoId(militar.getEstado() != null ? militar.getEstado().getId() : null);

        dto.setIdServidor(militar.getIdServidor());
        dto.setSubUnidadeId(militar.getSubUnidade() != null ? militar.getSubUnidade().getId() : null);
        dto.setSituacao(militar.getSituacao());
        dto.setPostoGraduacao(militar.getPostoGraduacao() != null ? militar.getPostoGraduacao().getLabel() : null);
        dto.setPostoGraduacaoEnum(militar.getPostoGraduacao() != null ? militar.getPostoGraduacao().name() : null);
        dto.setQuadro(militar.getQuadro());
        dto.setNumeroBarra(militar.getNumeroBarra());
        dto.setDataInclusao(militar.getDataInclusao());
        dto.setDataNascimento(militar.getDataNascimento());
        dto.setFuncaoId(militar.getFuncao() != null ? militar.getFuncao().getId() : null);
        dto.setServe31bpm(militar.getServe31bpm());
        dto.setSexo(militar.getSexo());
        dto.setCreatedAt(militar.getCreatedAt());

        dto.setEmail(militar.getEmail());
        dto.setLogradouro(militar.getLogradouro());
        dto.setNumeroCasa(militar.getNumeroCasa());
        dto.setBairro(militar.getBairro());

        return dto;
    }

    public Militar toEntity() {
        Militar militar = new Militar();
        militar.setId(this.id);
        militar.setNomeCompleto(this.nomeCompleto);
        militar.setNomeGuerra(this.nomeGuerra);
        militar.setMatricula(this.matricula);
        militar.setCpf(this.cpf);
        militar.setRgMilitar(this.rgMilitar);
        militar.setCelular(this.celular);

        if (this.cidadeId != null) {
            Cidade cidade = new Cidade();
            cidade.setId(this.cidadeId);
            militar.setCidade(cidade);
        }

        if (this.estadoId != null) {
            Estado estado = new Estado();
            estado.setId(this.estadoId);
            militar.setEstado(estado);
        }

        militar.setIdServidor(this.idServidor);

        if (this.subUnidadeId != null) {
            SubUnidade subUnidade = new SubUnidade();
            subUnidade.setId(this.subUnidadeId);
            militar.setSubUnidade(subUnidade);
        }

        militar.setSituacao(this.situacao);
        militar.setPostoGraduacao(PostoGraduacao.valueOf(this.postoGraduacaoEnum));
        militar.setQuadro(this.quadro);
        militar.setNumeroBarra(this.numeroBarra);
        militar.setDataInclusao(this.dataInclusao);
        militar.setDataNascimento(this.dataNascimento);

        if (this.funcaoId != null) {
            Funcao funcao = new Funcao();
            funcao.setId(this.funcaoId);
            militar.setFuncao(funcao);
        }

        militar.setServe31bpm(this.serve31bpm);
        militar.setSexo(this.sexo);
        militar.setCreatedAt(this.createdAt != null ? this.createdAt : LocalDateTime.now());

        militar.setEmail(this.email);
        militar.setLogradouro(this.logradouro);
        militar.setNumeroCasa(this.numeroCasa);
        militar.setBairro(this.bairro);

        return militar;
    }
}
