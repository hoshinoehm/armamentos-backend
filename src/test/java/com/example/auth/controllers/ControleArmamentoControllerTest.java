package com.example.auth.controllers;

import com.example.auth.config.ConfiguracaoUnidade;
import com.example.auth.domain.dto.ControleArmamentoProcessoVisualizacaoDTO;
import com.example.auth.domain.dto.DetalhesCautelaDTO;
import com.example.auth.infra.security.SecurityConfigurations;
import com.example.auth.infra.security.SecurityFilter;
import com.example.auth.infra.security.TokenService;
import com.example.auth.repositories.UserRepository;
import com.example.auth.services.ControleArmamentoService;
import com.example.auth.services.HtmlPdfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração do ControleArmamentoController via MockMvc.
 *
 * <p>Usa mocks para service e HtmlPdfService — não acessa banco nem renderiza PDF real.</p>
 * <p>SecurityFilter e TokenService são mockados para evitar validação JWT.</p>
 */
@WebMvcTest(ControleArmamentoController.class)
@Import({SecurityConfigurations.class, ConfiguracaoUnidade.class})
class ControleArmamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ControleArmamentoService controleArmamentoService;

    @MockBean
    private HtmlPdfService htmlPdfService;

    // Mocks necessários para a cadeia de segurança JWT
    @MockBean
    private SecurityFilter securityFilter;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserRepository userRepository;

    private static final byte[] PDF_BYTES = {0x25, 0x50, 0x44, 0x46}; // "%PDF"

    @BeforeEach
    void setUp() throws Exception {
        // HtmlPdfService sempre retorna bytes dummy
        given(htmlPdfService.render(anyString(), any(Context.class))).willReturn(PDF_BYTES);
        given(htmlPdfService.merge(
                any(byte[].class), any(byte[].class),
                any(byte[].class), any(byte[].class))
        ).willReturn(PDF_BYTES);
    }

    // ── REGRESSÃO: endpoint /relatorio (detalhes_cautela.jrxml) intocado ──

    @Test
    @DisplayName("GET /{id}/relatorio - sem autenticação deve retornar 403")
    void gerarRelatorio_semAutenticacao_retorna403() throws Exception {
        mockMvc.perform(get("/api/cautelas/1/relatorio"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /{id}/relatorio - com ADMIN deve tentar gerar relatório")
    @WithMockUser(roles = "ADMIN")
    void gerarRelatorio_comAdmin_executaEndpoint() throws Exception {
        DetalhesCautelaDTO dto = buildDetalhesCautelaMock();
        given(controleArmamentoService.buscarDetalhesPorId(1L)).willReturn(dto);

        // O endpoint pode retornar 200 (PDF gerado) ou 500 (JasperReports indisponível no slice)
        // O importante é que não seja 403 ou 404
        mockMvc.perform(get("/api/cautelas/1/relatorio"))
                .andExpect(result ->
                        org.junit.jupiter.api.Assertions.assertNotEquals(
                                403, result.getResponse().getStatus())
                )
                .andExpect(result ->
                        org.junit.jupiter.api.Assertions.assertNotEquals(
                                404, result.getResponse().getStatus())
                );
    }

    // ── NOVO: endpoint /relatorio-acaf-processo com HTML/PDF ──────────────

    @Test
    @DisplayName("GET /{id}/relatorio-acaf-processo - sem autenticação deve retornar 403")
    void gerarRelatorioAcafProcesso_semAutenticacao_retorna403() throws Exception {
        mockMvc.perform(get("/api/cautelas/1/relatorio-acaf-processo"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /{id}/relatorio-acaf-processo - com ADMIN deve retornar 200 e PDF")
    @WithMockUser(roles = "ADMIN")
    void gerarRelatorioAcafProcesso_comAdmin_retornaPdf() throws Exception {
        given(controleArmamentoService.getDadosParaRelatorio(1L)).willReturn(dadosProcessoMock());

        mockMvc.perform(get("/api/cautelas/1/relatorio-acaf-processo"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }

    @Test
    @DisplayName("GET /{id}/relatorio-acaf-processo - Content-Disposition deve conter o id")
    @WithMockUser(roles = "ADMIN")
    void gerarRelatorioAcafProcesso_contentDispositionContemId() throws Exception {
        given(controleArmamentoService.getDadosParaRelatorio(1L)).willReturn(dadosProcessoMock());

        mockMvc.perform(get("/api/cautelas/1/relatorio-acaf-processo"))
                .andExpect(header().string("Content-Disposition", containsString("acaf_processo_1")));
    }

    @Test
    @DisplayName("GET /{id}/relatorio-acaf-processo - service lança exceção deve retornar 500")
    @WithMockUser(roles = "ADMIN")
    void gerarRelatorioAcafProcesso_excecaoNoService_retorna500() throws Exception {
        given(controleArmamentoService.getDadosParaRelatorio(anyLong()))
                .willThrow(new RuntimeException("Cautela não encontrada"));

        mockMvc.perform(get("/api/cautelas/999/relatorio-acaf-processo"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("GET /{id}/relatorio-acaf-processo - ROLE_USER sem ADMIN deve retornar 403")
    @WithMockUser(roles = "USER")
    void gerarRelatorioAcafProcesso_roleUser_retorna403() throws Exception {
        mockMvc.perform(get("/api/cautelas/1/relatorio-acaf-processo"))
                .andExpect(status().isForbidden());
    }

    // ── helpers ────────────────────────────────────────────────────────────

    private DetalhesCautelaDTO buildDetalhesCautelaMock() {
        DetalhesCautelaDTO dto = new DetalhesCautelaDTO();
        dto.setPostoGraduacao("SD");
        dto.setNomeCompleto("FULANO SILVA");
        dto.setRgMilitar("12345");
        dto.setNumeroSerie("ABC123");
        dto.setTipo("PISTOLA");
        dto.setMarca("TAURUS");
        dto.setModelo("G2C");
        dto.setCalibre("9mm");
        dto.setNumeroTiro("17+1");
        dto.setCano("Curto");
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }

    private ControleArmamentoProcessoVisualizacaoDTO dadosProcessoMock() {
        ControleArmamentoProcessoVisualizacaoDTO d = new ControleArmamentoProcessoVisualizacaoDTO();
        d.setNomeCompleto("FULANO SILVA");
        d.setNomeGuerra("SILVA");
        d.setMatricula("123456");
        d.setCpf("11122233344");
        d.setRgMilitar("12345");
        d.setPostoGraduacao("SD");
        d.setQuadro("QP");
        d.setDataNascimento("01/01/1995");
        d.setDataInclusao("01/01/2020");
        d.setDataEntrega(java.time.LocalDate.of(2026, 5, 15));
        d.setCidade("Gov. Nunes Freire");
        d.setEstado("MA");
        d.setEmail("fulano@pm.ma.gov.br");
        d.setTelefone("98900000000");
        d.setEnderecoCompleto("Rua X, 1, Centro");
        d.setSubUnidade("1ª CIA");
        d.setIdentificacaoCompleta("SD QP FULANO SILVA");
        d.setEspecie("PISTOLA SEMI-AUTOMATICA");
        d.setMarca("TAURUS");
        d.setModelo("G2C");
        d.setCalibre("9mm");
        d.setNumeroSerie("ABC123");
        d.setNumeroTiro("17+1");
        d.setCano("Curto");
        d.setEstadoConservacao("Bom");
        d.setTombo("5678");
        d.setQuantidadeCarregadores(2);
        d.setQuantidadeMunicao(17);
        d.setObservacoes("");
        return d;
    }
}
