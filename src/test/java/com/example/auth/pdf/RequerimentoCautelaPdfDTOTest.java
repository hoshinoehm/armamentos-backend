package com.example.auth.pdf;

import com.example.auth.config.ConfiguracaoUnidade;
import com.example.auth.domain.dto.ControleArmamentoProcessoVisualizacaoDTO;
import com.example.auth.domain.dto.RequerimentoCautelaPdfDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes unitários para RequerimentoCautelaPdfDTO.from().
 * Verifica campos derivados e mapeamentos críticos — sem Spring context.
 */
class RequerimentoCautelaPdfDTOTest {

    private ControleArmamentoProcessoVisualizacaoDTO dados;
    private ConfiguracaoUnidade config;

    @BeforeEach
    void setUp() {
        dados = new ControleArmamentoProcessoVisualizacaoDTO();
        dados.setNomeCompleto("THIAGO YACOB ANDRADE SANTIAGO");
        dados.setNomeGuerra("SANTIAGO");
        dados.setMatricula("887805");
        dados.setCpf("12345678901");
        dados.setRgMilitar("22814");
        dados.setPostoGraduacao("2TEN");
        dados.setQuadro("QOEM");
        dados.setDataNascimento("15/03/1990");
        dados.setDataInclusao("26/06/2017");
        dados.setDataEntrega(java.time.LocalDate.of(2026, 5, 15));
        dados.setCidade("Gov. Nunes Freire");
        dados.setEstado("MA");
        dados.setEmail("thiago@pm.ma.gov.br");
        dados.setTelefone("98991234567");
        dados.setEnderecoCompleto("Rua das Flores, 123, Centro, Gov. Nunes Freire - MA");
        dados.setSubUnidade("1ª CIA");
        dados.setIdentificacaoCompleta("2TEN QOEM THIAGO YACOB ANDRADE SANTIAGO");

        dados.setEspecie("PISTOLA SEMI-AUTOMATICA");
        dados.setMarca("TAURUS");
        dados.setModelo("PT 840");
        dados.setCalibre(".40");
        dados.setNumeroSerie("SJZ06578");
        dados.setNumeroTiro("15+1");
        dados.setCano("Médio");
        dados.setEstadoConservacao("Bom");
        dados.setTombo("1234");
        dados.setQuantidadeCarregadores(2);

        dados.setQuantidadeMunicao(50);
        dados.setObservacoes("Nenhuma");

        config = new ConfiguracaoUnidade();
        config.setOpm("31º BPM");
        config.setUf("MA");
        config.setCidade("Gov. Nunes Freire");
        config.setDestinatario("Excelentíssimo Senhor Cel QOEM Comandante do CPAI/8");
        config.setPrazoCautela("indeterminado");
        config.setDocumentosComprobatorios("- Cópia da Identidade Militar e do CPF.");
        config.setNomeChefeP4("JOAO SILVA");
        config.setPostoChefeP4("CAP QOEM");
        config.setFuncaoChefeP4("Chefe do P/4 do 31º BPM");
        config.setNomeComandanteOpm("PEDRO SOUZA");
        config.setPostoComandanteOpm("TC QOEM");
        config.setFuncaoComandanteOpm("Cmt do 31º BPM");
        config.setDespachoComandanteOpm("Encaminhe-se ao Cmt do CPA I-8.");
    }

    @Test
    @DisplayName("protocolo deve ser derivado do ID e do ano da dataEntrega")
    void protocolo_derivadoDoCautelaIdEAno() {
        RequerimentoCautelaPdfDTO r = RequerimentoCautelaPdfDTO.from(dados, config, 7L);
        assertThat(r.getProtocolo()).isEqualTo("007/2026");
    }

    @Test
    @DisplayName("anoEntrega deve ser extraído da dataEntrega dd/MM/yyyy")
    void anoEntrega_extraidoDaData() {
        RequerimentoCautelaPdfDTO r = RequerimentoCautelaPdfDTO.from(dados, config, 1L);
        assertThat(r.getAnoEntrega()).isEqualTo(2026);
    }

    @Test
    @DisplayName("rgUf deve concatenar rgMilitar com -PMMA")
    void rgUf_concatenadoComSufixo() {
        RequerimentoCautelaPdfDTO r = RequerimentoCautelaPdfDTO.from(dados, config, 1L);
        assertThat(r.getRgUf()).isEqualTo("22814-PMMA");
    }

    @Test
    @DisplayName("numeroArma deve ser mapeado do campo numeroSerie")
    void numeroArma_mapeadoDeNumeroSerie() {
        RequerimentoCautelaPdfDTO r = RequerimentoCautelaPdfDTO.from(dados, config, 1L);
        assertThat(r.getNumeroArma()).isEqualTo("SJZ06578");
    }

    @Test
    @DisplayName("dataRequerimento deve ser igual a dataEntrega do DTO de origem")
    void dataRequerimento_igualDataEntrega() {
        RequerimentoCautelaPdfDTO r = RequerimentoCautelaPdfDTO.from(dados, config, 1L);
        assertThat(r.getDataRequerimento()).isEqualTo("15/05/2026");
    }

    @Test
    @DisplayName("campos da unidade devem vir da ConfiguracaoUnidade")
    void camposUnidade_vindosDaConfig() {
        RequerimentoCautelaPdfDTO r = RequerimentoCautelaPdfDTO.from(dados, config, 1L);
        assertThat(r.getOpm()).isEqualTo("31º BPM");
        assertThat(r.getUf()).isEqualTo("MA");
        assertThat(r.getPostoChefeP4()).isEqualTo("CAP QOEM");
        assertThat(r.getNomeChefeP4()).isEqualTo("JOAO SILVA");
        assertThat(r.getPostoComandanteOpm()).isEqualTo("TC QOEM");
        assertThat(r.getDespachoComandanteOpm()).isEqualTo("Encaminhe-se ao Cmt do CPA I-8.");
    }

    @Test
    @DisplayName("situacaoJuridica deve ter valor padrão")
    void situacaoJuridica_valorPadrao() {
        RequerimentoCautelaPdfDTO r = RequerimentoCautelaPdfDTO.from(dados, config, 1L);
        assertThat(r.getSituacaoJuridica()).isNotBlank();
    }

    @Test
    @DisplayName("dataInclusao deve ser mapeado do DTO de origem")
    void dataInclusao_mapeado() {
        RequerimentoCautelaPdfDTO r = RequerimentoCautelaPdfDTO.from(dados, config, 1L);
        assertThat(r.getDataInclusao()).isEqualTo("26/06/2017");
    }

    @Test
    @DisplayName("quando dataEntrega é nula anoEntrega usa o ano atual")
    void anoEntrega_fallbackAnoAtual_quandoDataNula() {
        dados.setDataEntrega(null);
        RequerimentoCautelaPdfDTO r = RequerimentoCautelaPdfDTO.from(dados, config, 1L);
        assertThat(r.getAnoEntrega()).isGreaterThan(2020);
    }
}
