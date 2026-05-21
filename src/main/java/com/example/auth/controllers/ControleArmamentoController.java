package com.example.auth.controllers;

import com.example.auth.config.ConfiguracaoUnidade;
import com.example.auth.domain.dto.ControleArmamentoCadastroDTO;
import com.example.auth.domain.dto.ControleArmamentoProcessoVisualizacaoDTO;
import com.example.auth.domain.dto.ControleArmamentoVisualizacaoDTO;
import com.example.auth.domain.dto.DetalhesCautelaDTO;
import com.example.auth.domain.dto.RequerimentoCautelaPdfDTO;
import com.example.auth.services.ControleArmamentoService;
import com.example.auth.services.HtmlPdfService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cautelas")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ControleArmamentoController {

    private final ControleArmamentoService controleArmamentoService;
    private final HtmlPdfService htmlPdfService;
    private final ConfiguracaoUnidade configuracaoUnidade;

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

    // ── Relatório simples (detalhes_cautela.jrxml) — MANTIDO INTOCADO ──────

    @GetMapping("/{id}/relatorio")
    public ResponseEntity<?> gerarRelatorio(@PathVariable Long id) {
        try {
            DetalhesCautelaDTO dados = controleArmamentoService.buscarDetalhesPorId(id);

            InputStream reportStream = getClass().getResourceAsStream("/reports/detalhes_cautela.jrxml");
            if (reportStream == null) {
                return ResponseEntity
                        .status(500)
                        .body("Arquivo 'detalhes_cautela.jrxml' não encontrado no classpath.");
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
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body("Erro ao gerar o relatório: " + e.getMessage());
        }
    }

    // ── Endpoint auxiliar: dados brutos para debug ──────────────────────────

    @GetMapping("/{id}/processo/relatorio")
    public ControleArmamentoProcessoVisualizacaoDTO getRelatorio(@PathVariable Long id) {
        return controleArmamentoService.getDadosParaRelatorio(id);
    }

    // ── Relatório ACAF completo (8 páginas) — HTML/PDF via OpenHTMLToPDF ───

    @GetMapping("/{id}/relatorio-acaf-processo")
    public ResponseEntity<?> gerarRelatorioAcafProcesso(@PathVariable Long id) {
        try {
            ControleArmamentoProcessoVisualizacaoDTO dados =
                    controleArmamentoService.getDadosParaRelatorio(id);

            RequerimentoCautelaPdfDTO r =
                    RequerimentoCautelaPdfDTO.from(dados, configuracaoUnidade, id);

            Context ctx = new Context();
            ctx.setVariable("r", r);

            // Renderiza o template principal
            byte[] merged = htmlPdfService.render("requerimento_cautela", ctx);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "inline; filename=acaf_processo_" + id + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(merged);

        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body("Erro ao gerar o relatório ACAF: " + e.getMessage());
        }
    }
}
