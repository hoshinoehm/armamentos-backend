package com.example.auth.pdf;

import com.example.auth.config.ConfiguracaoUnidade;
import com.example.auth.domain.dto.ControleArmamentoProcessoVisualizacaoDTO;
import com.example.auth.domain.dto.RequerimentoCautelaPdfDTO;
import com.example.auth.services.HtmlPdfService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.context.Context;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

/**
 * Testes do HtmlPdfService em contexto mínimo (sem banco de dados).
 * Apenas Thymeleaf + HtmlPdfService + ConfiguracaoUnidade são carregados.
 */
@SpringBootTest(
        classes = {HtmlPdfService.class, ConfiguracaoUnidade.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@Import(ThymeleafAutoConfiguration.class)
@TestPropertySource(properties = {
        "unidade.opm=31\u00ba BPM",
        "unidade.uf=MA",
        "unidade.cidade=Gov. Nunes Freire",
        "unidade.destinatario=Excelent\u00edssimo Senhor Cel QOEM Comandante do CPAI/8",
        "unidade.prazo-cautela=indeterminado",
        "unidade.documentos-comprobatorios=- C\u00f3pia da Identidade Militar e do CPF.",
        "unidade.nome-chefe-p4=JOAO SILVA",
        "unidade.posto-chefe-p4=CAP QOEM",
        "unidade.funcao-chefe-p4=Chefe do P/4 do 31\u00ba BPM",
        "unidade.nome-comandante-opm=PEDRO SOUZA",
        "unidade.posto-comandante-opm=TC QOEM",
        "unidade.funcao-comandante-opm=Cmt do 31\u00ba BPM",
        "unidade.despacho-comandante-opm=Encaminhe-se ao Cmt do CPA I-8."
})
class HtmlPdfServiceTest {

    @Autowired
    private HtmlPdfService htmlPdfService;

    @Autowired
    private ConfiguracaoUnidade configuracaoUnidade;

    private Context buildContext() {
        RequerimentoCautelaPdfDTO r = RequerimentoCautelaPdfDTO.from(dadosMock(), configuracaoUnidade, 1L);
        Context ctx = new Context();
        ctx.setVariable("r", r);
        return ctx;
    }

    @Test
    @DisplayName("requerimento_cautela.html deve gerar PDF com bytes > 0")
    void renderRequerimentoCautela_geraBytes() throws Exception {
        byte[] pdf = htmlPdfService.render("requerimento_cautela", buildContext());
        assertThat(pdf).isNotNull().hasSizeGreaterThan(100);
    }

    @Test
    @DisplayName("permanencia_cautela.html deve gerar PDF com bytes > 0")
    void renderPermanenciaCautela_geraBytes() throws Exception {
        byte[] pdf = htmlPdfService.render("permanencia_cautela", buildContext());
        assertThat(pdf).isNotNull().hasSizeGreaterThan(100);
    }

    @Test
    @DisplayName("cautela_arma_geral.html deve gerar PDF com bytes > 0")
    void renderCautelaArmaGeral_geraBytes() throws Exception {
        byte[] pdf = htmlPdfService.render("cautela_arma_geral", buildContext());
        assertThat(pdf).isNotNull().hasSizeGreaterThan(100);
    }

    @Test
    @DisplayName("observacoes_arma.html deve gerar PDF com bytes > 0")
    void renderObservacoesArma_geraBytes() throws Exception {
        byte[] pdf = htmlPdfService.render("observacoes_arma", buildContext());
        assertThat(pdf).isNotNull().hasSizeGreaterThan(100);
    }

    @Test
    @DisplayName("merge de 4 PDFs deve gerar PDF maior que qualquer página individual")
    void merge_quatroPdfs_geraPdfCombinado() throws Exception {
        Context ctx = buildContext();
        byte[] p1 = htmlPdfService.render("requerimento_cautela", ctx);
        byte[] p2 = htmlPdfService.render("permanencia_cautela", ctx);
        byte[] p3 = htmlPdfService.render("cautela_arma_geral", ctx);
        byte[] p4 = htmlPdfService.render("observacoes_arma", ctx);

        byte[] merged = htmlPdfService.merge(p1, p2, p3, p4);
        assertThat(merged).isNotNull().hasSizeGreaterThan(p1.length);
    }

    @Test
    @DisplayName("render não deve lançar exceção com DTO mínimo preenchido")
    void render_semExcecao_comDtoMinimo() {
        assertThatNoException().isThrownBy(() ->
                htmlPdfService.render("requerimento_cautela", buildContext())
        );
    }

    // ── helpers ────────────────────────────────────────────────────────────

    private ControleArmamentoProcessoVisualizacaoDTO dadosMock() {
        ControleArmamentoProcessoVisualizacaoDTO d = new ControleArmamentoProcessoVisualizacaoDTO();
        d.setNomeCompleto("THIAGO YACOB ANDRADE SANTIAGO");
        d.setNomeGuerra("SANTIAGO");
        d.setMatricula("887805");
        d.setCpf("12345678901");
        d.setRgMilitar("22814");
        d.setPostoGraduacao("2TEN");
        d.setQuadro("QOEM");
        d.setDataNascimento("15/03/1990");
        d.setDataInclusao("26/06/2017");
        d.setDataEntrega(java.time.LocalDate.of(2026, 5, 15));
        d.setCidade("Gov. Nunes Freire");
        d.setEstado("MA");
        d.setEmail("thiago@pm.ma.gov.br");
        d.setTelefone("98991234567");
        d.setEnderecoCompleto("Rua das Flores, 123, Centro, Gov. Nunes Freire - MA");
        d.setSubUnidade("1ª CIA");
        d.setIdentificacaoCompleta("2TEN QOEM THIAGO YACOB ANDRADE SANTIAGO");
        d.setEspecie("PISTOLA SEMI-AUTOMATICA");
        d.setMarca("TAURUS");
        d.setModelo("PT 840");
        d.setCalibre(".40");
        d.setNumeroSerie("SJZ06578");
        d.setNumeroTiro("15+1");
        d.setCano("Médio");
        d.setEstadoConservacao("Bom");
        d.setTombo("1234");
        d.setQuantidadeCarregadores(2);
        d.setQuantidadeMunicao(50);
        d.setObservacoes("Nenhuma");
        return d;
    }
}
