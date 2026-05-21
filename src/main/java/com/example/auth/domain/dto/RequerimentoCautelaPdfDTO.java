package com.example.auth.domain.dto;

import com.example.auth.config.ConfiguracaoUnidade;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * DTO flat consumido pelos templates Thymeleaf de geração de PDF.
 * Variável exposta nos templates como {@code r}.
 */
@Data
public class RequerimentoCautelaPdfDTO {

    // ── Identificação do requerimento ──────────────────────────────────────
    private String protocolo;          // ex: "001/2026"
    private String opm;                // ex: "31º BPM"
    private String cidade;             // cidade da OPM
    private String uf;                 // ex: "MA"
    private String dataRequerimento;   // dd/MM/yyyy

    // ── Dados do militar ──────────────────────────────────────────────────
    private String nomeCompleto;
    private String nomeGuerra;
    private String postoGraduacao;
    private String quadro;
    private String matricula;
    private String cpf;
    private String rgUf;               // ex: "22.814-PMMA"
    private String telefone;
    private String email;
    private String endereco;
    private String dataNascimento;
    private String dataInclusao;
    private String comportamento;
    private String situacaoJuridica;
    private String subUnidade;
    private String identificacaoCompleta;  // "POSTO QUADRO NOME"
    private String cidadeResidencia;
    private String cidadeTrabalho;

    // ── Dados da arma ─────────────────────────────────────────────────────
    private String especie;            // ex: "PISTOLA SEMI-AUTOMATICA"
    private String marca;
    private String modelo;
    private String calibre;
    private String numeroArma;         // número de série
    private String paisFabricacao;
    private String quantidadeTiros;
    private String nivelProtecao;
    private String cano;
    private String tombo;
    private String estadoConservacao;
    private Integer quantidadeCarregadores;

    // ── Dados do processo ─────────────────────────────────────────────────
    private String destinatario;
    private String prazoCautela;
    private String documentosComprobatorios;
    private Integer quantidadeMunicao;
    private String observacoes;

    // ── Assinaturas / despachos ───────────────────────────────────────────
    private String nomeChefeP4;
    private String postoChefeP4;
    private String funcaoChefeP4;
    private String nomeComandanteOpm;
    private String postoComandanteOpm;
    private String funcaoComandanteOpm;
    private String despachoComandanteOpm;

    // ── Campos derivados para cautela_arma_geral / permanencia ────────────
    private Long cautelaId;
    private Integer anoEntrega;

    // ──────────────────────────────────────────────────────────────────────

    private static final DateTimeFormatter FMT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Constrói o DTO a partir dos dados operacionais + configuração da unidade.
     *
     * @param dados     dados do processo (militar + arma + controle)
     * @param config    configuração fixa da unidade (chefes, etc.)
     * @param cautelaId id da cautela (para protocolo e numeração)
     */
    public static RequerimentoCautelaPdfDTO from(
            ControleArmamentoProcessoVisualizacaoDTO dados,
            ConfiguracaoUnidade config,
            Long cautelaId) {

        RequerimentoCautelaPdfDTO r = new RequerimentoCautelaPdfDTO();

        // ── Identificação ──
        r.cautelaId = cautelaId;
        r.uf = config.getUf();
        r.cidade = config.getCidade();
        r.opm = config.getOpm();
        r.dataRequerimento = dados.getDataEntrega();

        // Protocolo: "001/2026" derivado do ID e ano da data de entrega
        int ano = extrairAno(dados.getDataEntrega());
        r.anoEntrega = ano;
        r.protocolo = String.format("%03d/%d", cautelaId, ano);

        // ── Militar ──
        r.nomeCompleto = dados.getNomeCompleto();
        r.nomeGuerra = dados.getNomeGuerra();
        r.postoGraduacao = dados.getPostoGraduacao();
        r.quadro = dados.getQuadro();
        r.matricula = dados.getMatricula();
        r.cpf = dados.getCpf();
        r.rgUf = nullToEmpty(dados.getRgMilitar()) + "-PMMA";
        r.telefone = dados.getTelefone();
        r.email = dados.getEmail();
        r.endereco = dados.getEnderecoCompleto();
        r.dataNascimento = dados.getDataNascimento();
        r.dataInclusao = dados.getDataInclusao();
        r.comportamento = nullToDefault(dados.getComportamento(), "N\u00e3o informado");
        r.situacaoJuridica = nullToDefault(dados.getSituacaoJuridica(), "Sem Altera\u00e7\u00e3o");
        r.subUnidade = dados.getSubUnidade();
        r.identificacaoCompleta = dados.getIdentificacaoCompleta();
        r.cidadeResidencia = dados.getCidade();
        String subUnidade = dados.getSubUnidade();
        r.cidadeTrabalho = subUnidade != null && subUnidade.contains("–")
                ? subUnidade.substring(subUnidade.lastIndexOf("–") + 1).trim()
                : subUnidade;

        // ── Arma ──
        r.especie = dados.getEspecie();
        r.marca = dados.getMarca();
        r.modelo = dados.getModelo();
        r.calibre = dados.getCalibre();
        r.numeroArma = dados.getNumeroSerie();
        r.paisFabricacao = "BRASIL";
        r.quantidadeTiros = dados.getNumeroTiro();
        r.nivelProtecao = "";
        r.cano = dados.getCano();
        r.tombo = dados.getTombo();
        r.estadoConservacao = dados.getEstadoConservacao();
        r.quantidadeCarregadores = dados.getQuantidadeCarregadores();

        // ── Processo ──
        r.destinatario = config.getDestinatario();
        r.prazoCautela = config.getPrazoCautela();
        r.documentosComprobatorios = config.getDocumentosComprobatorios();
        r.quantidadeMunicao = dados.getQuantidadeMunicao();
        r.observacoes = dados.getObservacoes();

        // ── Assinaturas ──
        r.nomeChefeP4 = config.getNomeChefeP4();
        r.postoChefeP4 = config.getPostoChefeP4();
        r.funcaoChefeP4 = config.getFuncaoChefeP4();
        r.nomeComandanteOpm = config.getNomeComandanteOpm();
        r.postoComandanteOpm = config.getPostoComandanteOpm();
        r.funcaoComandanteOpm = config.getFuncaoComandanteOpm();
        r.despachoComandanteOpm = config.getDespachoComandanteOpm();

        return r;
    }

    private static int extrairAno(String dataFormatada) {
        if (dataFormatada == null || dataFormatada.length() < 10) {
            return LocalDate.now().getYear();
        }
        try {
            return LocalDate.parse(dataFormatada, FMT_DATE).getYear();
        } catch (Exception e) {
            return LocalDate.now().getYear();
        }
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private static String nullToDefault(String value, String defaultValue) {
        return (value == null || value.isBlank()) ? defaultValue : value;
    }
}
