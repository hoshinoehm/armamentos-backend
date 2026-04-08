package com.example.auth.controllers;


import com.example.auth.domain.dto.ControleArmamentoCadastroDTO;
import com.example.auth.domain.dto.ControleArmamentoProcessoVisualizacaoDTO;
import com.example.auth.domain.dto.ControleArmamentoVisualizacaoDTO;
import com.example.auth.domain.dto.DetalhesCautelaDTO;
import com.example.auth.services.ControleArmamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cautelas")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ControleArmamentoController {

    private final ControleArmamentoService controleArmamentoService;

    @GetMapping
    public ResponseEntity<List<ControleArmamentoVisualizacaoDTO>> listarTodos() {
        List<ControleArmamentoVisualizacaoDTO> lista = controleArmamentoService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/nao-baixadas")
    public ResponseEntity<List<ControleArmamentoVisualizacaoDTO>> listarNaoBaixadas() {
        List<ControleArmamentoVisualizacaoDTO> lista = controleArmamentoService.listarNaoBaixadas();
        return ResponseEntity.ok(lista);
    }


    @PostMapping
    public ResponseEntity<ControleArmamentoVisualizacaoDTO> salvar(@Valid @RequestBody ControleArmamentoCadastroDTO dto) {
        ControleArmamentoVisualizacaoDTO salvo = controleArmamentoService.salvar(dto);
        return ResponseEntity.ok(salvo);
    }


    @PatchMapping("/{id}/baixa")
    public ResponseEntity<Void> darBaixa(@PathVariable Long id) {
        controleArmamentoService.darBaixaNaCautela(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<ControleArmamentoVisualizacaoDTO> buscarPorId(@PathVariable Long id) {
        ControleArmamentoVisualizacaoDTO dto = controleArmamentoService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/relatorio")
    public ResponseEntity<?> gerarRelatorio(@PathVariable Long id) {
        try {
            DetalhesCautelaDTO dados = controleArmamentoService.buscarDetalhesPorId(id);

            InputStream reportStream = getClass().getResourceAsStream("/reports/detalhes_cautela.jrxml");
            if (reportStream == null) {
                return ResponseEntity
                        .status(500)
                        .body("Arquivo 'detalhes_cautela.jrxml' não encontrado no classpath. Verifique se ele está em 'src/main/resources/reports/'.");
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("postoGraduacao", dados.getPostoGraduacao().toString());
            parametros.put("nomeCompleto", dados.getNomeCompleto());
            parametros.put("rgMilitar", dados.getRgMilitar());
            parametros.put("numeroSerie", dados.getNumeroSerie());
            parametros.put("tipo", dados.getTipo());
            parametros.put("marca", dados.getMarca());
            parametros.put("modelo", dados.getModelo());
            parametros.put("calibre", dados.getCalibre());
            parametros.put("numeroTiro", dados.getNumeroTiro());
            parametros.put("cano", dados.getCano());
            parametros.put("createdAt", java.sql.Date.valueOf(dados.getCreatedAt().toLocalDate()));

            JasperPrint print = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
            byte[] pdf = JasperExportManager.exportReportToPdf(print);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "inline; filename=cautela_" + id + ".pdf")
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body("Erro ao gerar o relatório: " + e.getMessage());
        }
    }



    @GetMapping("/{id}/processo/relatorio")
    public ControleArmamentoProcessoVisualizacaoDTO getRelatorio(@PathVariable Long id) {
        return controleArmamentoService.getDadosParaRelatorio(id);
    }


    @GetMapping("/{id}/relatorio-acaf-processo")
    public ResponseEntity<byte[]> gerarRelatorioAcafProcesso(@PathVariable Long id) throws Exception {
        ControleArmamentoProcessoVisualizacaoDTO dados = controleArmamentoService.getDadosParaRelatorio(id);

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("nomeCompleto", dados.getNomeCompleto());
        parametros.put("nomeGuerra", dados.getNomeGuerra());
        parametros.put("matricula", dados.getMatricula());
        parametros.put("rgMilitar", dados.getRgMilitar());
        parametros.put("cpf", dados.getCpf());
        parametros.put("postoGraduacao", dados.getPostoGraduacao());
        parametros.put("quadro", dados.getQuadro());
        parametros.put("dataNascimento", dados.getDataNascimento());
        parametros.put("subUnidade", dados.getSubUnidade());
        parametros.put("cidade", dados.getCidade());
        parametros.put("estado", dados.getEstado());
        parametros.put("email", dados.getEmail());
        parametros.put("telefone", dados.getTelefone());
        parametros.put("enderecoCompleto", dados.getEnderecoCompleto());
        parametros.put("especie", dados.getEspecie());
        parametros.put("marca", dados.getMarca());
        parametros.put("modelo", dados.getModelo());
        parametros.put("calibre", dados.getCalibre());
        parametros.put("numeroSerie", dados.getNumeroSerie());
        parametros.put("numeroTiro", dados.getNumeroTiro());
        parametros.put("cano", dados.getCano());
        parametros.put("estadoConservacao", dados.getEstadoConservacao());
        parametros.put("tombo", dados.getTombo());
        parametros.put("quantidadeCarregadores", dados.getQuantidadeCarregadores());
        parametros.put("quantidadeMunicao", dados.getQuantidadeMunicao());
        parametros.put("dataEntrega", dados.getDataEntrega());
        parametros.put("observacoes", dados.getObservacoes());

        InputStream processoStream = getClass().getResourceAsStream("/reports/acaf_processo_relatorio.jrxml");
        JasperReport processoReport = JasperCompileManager.compileReport(processoStream);
        JasperPrint processoPrint = JasperFillManager.fillReport(processoReport, parametros, new JREmptyDataSource());

        InputStream amparoStream = getClass().getResourceAsStream("/reports/amparo_legal.jrxml");
        JasperReport amparoReport = JasperCompileManager.compileReport(amparoStream);
        JasperPrint amparoPrint = JasperFillManager.fillReport(amparoReport, new HashMap<>(), new JREmptyDataSource());

        InputStream requerimentoStream = getClass().getResourceAsStream("/reports/requerimento_cautela_externa.jrxml");
        JasperReport requerimentoReport = JasperCompileManager.compileReport(requerimentoStream);
        Map<String, Object> parametrosRequerimento = new HashMap<>();
        parametrosRequerimento.put("cidade", dados.getCidade());
        parametrosRequerimento.put("estado", dados.getEstado());
        parametrosRequerimento.put("especie", dados.getEspecie());
        parametrosRequerimento.put("marca", dados.getMarca());
        parametrosRequerimento.put("calibre", dados.getCalibre());
        parametrosRequerimento.put("dataEntrega", dados.getDataEntrega());
        parametrosRequerimento.put("postoGraduacao", dados.getPostoGraduacao());
        parametrosRequerimento.put("quadro", dados.getQuadro());
        parametrosRequerimento.put("nomeCompleto", dados.getNomeCompleto());
        JasperPrint requerimentoPrint = JasperFillManager.fillReport(requerimentoReport, parametrosRequerimento, new JREmptyDataSource());

        InputStream parecerStream = getClass().getResourceAsStream("/reports/parecer_comandante_upm.jrxml");
        JasperReport parecerReport = JasperCompileManager.compileReport(parecerStream);
        Map<String, Object> parametrosParecer = new HashMap<>();
        parametrosParecer.put("cidade", dados.getCidade());
        parametrosParecer.put("estado", dados.getEstado());
        parametrosParecer.put("especie", dados.getEspecie());
        parametrosParecer.put("marca", dados.getMarca());
        parametrosParecer.put("calibre", dados.getCalibre());
        parametrosParecer.put("dataEntrega", dados.getDataEntrega());
        parametrosParecer.put("postoGraduacao", dados.getPostoGraduacao());
        parametrosParecer.put("quadro", dados.getQuadro());
        parametrosParecer.put("nomeCompleto", dados.getNomeCompleto());
        parametrosParecer.put("identificacaoCompleta", dados.getIdentificacaoCompleta());
        JasperPrint parecerPrint = JasperFillManager.fillReport(parecerReport, parametrosParecer, new JREmptyDataSource());

        InputStream concordoStream = getClass().getResourceAsStream("/reports/concordo_comandante.jrxml");
        JasperReport concordoReport = JasperCompileManager.compileReport(concordoStream);
        Map<String, Object> parametrosConcordo = new HashMap<>();
        parametrosConcordo.put("especie", dados.getEspecie());
        parametrosConcordo.put("marca", dados.getMarca());
        parametrosConcordo.put("calibre", dados.getCalibre());
        parametrosConcordo.put("identificacaoCompleta", dados.getIdentificacaoCompleta());
        parametrosConcordo.put("dataEntrega", dados.getDataEntrega());
        JasperPrint concordoPrint = JasperFillManager.fillReport(concordoReport, parametrosConcordo, new JREmptyDataSource());

        InputStream permanenciaStream = getClass().getResourceAsStream("/reports/permanencia_cautela.jrxml");
        JasperReport permanenciaReport = JasperCompileManager.compileReport(permanenciaStream);
        Map<String, Object> parametrosPermanencia = new HashMap<>();
        parametrosPermanencia.put("cidade", dados.getCidade());
        parametrosPermanencia.put("estado", dados.getEstado());
        parametrosPermanencia.put("especie", dados.getEspecie());
        parametrosPermanencia.put("marca", dados.getMarca());
        parametrosPermanencia.put("modelo", dados.getModelo());
        parametrosPermanencia.put("calibre", dados.getCalibre());
        parametrosPermanencia.put("dataEntrega", dados.getDataEntrega());
        String dataEntregaStr = dados.getDataEntrega();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataEntrega = LocalDate.parse(dataEntregaStr, formatter);
        int anoEntrega = dataEntrega.getYear();
        parametrosPermanencia.put("anoEntrega", anoEntrega);
        parametrosPermanencia.put("postoGraduacao", dados.getPostoGraduacao());
        parametrosPermanencia.put("quadro", dados.getQuadro());
        parametrosPermanencia.put("nomeCompleto", dados.getNomeCompleto());
        parametrosPermanencia.put("identificacaoCompleta", dados.getIdentificacaoCompleta());
        parametrosPermanencia.put("numeroSerie", dados.getNumeroSerie());
        parametrosPermanencia.put("cautelaId", id);
        JasperPrint permanenciaPrint = JasperFillManager.fillReport(permanenciaReport, parametrosPermanencia, new JREmptyDataSource());

        InputStream cautelaStream = getClass().getResourceAsStream("/reports/cautela_arma_geral.jrxml");
        JasperReport cautelaReport = JasperCompileManager.compileReport(cautelaStream);
        Map<String, Object> parametrosCautela = new HashMap<>();
        parametrosCautela.put("cidade", dados.getCidade());
        parametrosCautela.put("estado", dados.getEstado());
        parametrosCautela.put("especie", dados.getEspecie());
        parametrosCautela.put("marca", dados.getMarca());
        parametrosCautela.put("calibre", dados.getCalibre());
        parametrosCautela.put("dataEntrega", dados.getDataEntrega());
        parametrosCautela.put("postoGraduacao", dados.getPostoGraduacao());
        parametrosCautela.put("quadro", dados.getQuadro());
        parametrosCautela.put("nomeCompleto", dados.getNomeCompleto());
        parametrosCautela.put("identificacaoCompleta", dados.getIdentificacaoCompleta());
        parametrosCautela.put("cautelaId", id);
        parametrosCautela.put("anoEntrega", anoEntrega);
        parametrosCautela.put("modelo", dados.getModelo());
        parametrosCautela.put("numeroSerie", dados.getNumeroSerie());
        parametrosCautela.put("nomeGuerra", dados.getNomeGuerra());
        parametrosCautela.put("matricula", dados.getMatricula());
        parametrosCautela.put("rgMilitar", dados.getRgMilitar());
        parametrosCautela.put("subUnidade", dados.getSubUnidade());
        parametrosCautela.put("numeroTiro", dados.getNumeroTiro());
        parametrosCautela.put("tombo", dados.getTombo());
        parametrosCautela.put("quantidadeCarregadores", dados.getQuantidadeCarregadores());
        parametrosCautela.put("estadoConservacao", dados.getEstadoConservacao());
        parametrosCautela.put("cano", dados.getCano());
        JasperPrint cautelaPrint = JasperFillManager.fillReport(cautelaReport, parametrosCautela, new JREmptyDataSource());

        InputStream observacoesStream = getClass().getResourceAsStream("/reports/observacoes_arma.jrxml");
        JasperReport observacoesReport = JasperCompileManager.compileReport(observacoesStream);
        Map<String, Object> parametrosObservacoes = new HashMap<>();
        parametrosObservacoes.put("dataEntrega", dados.getDataEntrega());
        parametrosObservacoes.put("identificacaoCompleta", dados.getIdentificacaoCompleta());
        JasperPrint observacoesPrint = JasperFillManager.fillReport(observacoesReport, parametrosObservacoes, new JREmptyDataSource());

        List<JasperPrint> allPrints = List.of(
                processoPrint,
                amparoPrint,
                requerimentoPrint,
                parecerPrint,
                concordoPrint,
                permanenciaPrint,
                cautelaPrint,
                observacoesPrint
        );
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(SimpleExporterInput.getInstance(allPrints));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
        exporter.exportReport();

        return ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=acaf_processo_" + id + ".pdf")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(baos.toByteArray());
    }




}
