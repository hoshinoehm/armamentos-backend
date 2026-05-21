package com.example.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "unidade")
@Data
public class ConfiguracaoUnidade {

    private String opm = "31\u00ba BPM";
    private String uf = "MA";
    private String cidade = "Gov. Nunes Freire";
    private String destinatario = "Excelent\u00edssimo Senhor Cel QOEM Comandante do CPAI/8";
    private String prazoCautela = "indeterminado";
    private String documentosComprobatorios = "- C\u00f3pia da Identidade Militar e do CPF, Exposi\u00e7\u00e3o de motivos.";
    private String nomeChefeP4 = "";
    private String postoChefeP4 = "CAP QOEM";
    private String funcaoChefeP4 = "Chefe do P/4 do 31\u00ba BPM";
    private String nomeComandanteOpm = "";
    private String postoComandanteOpm = "TC QOEM";
    private String funcaoComandanteOpm = "Cmt do 31\u00ba BPM";
    private String despachoComandanteOpm = "Encaminhe-se ao Cmt do CPA I-8.";
}
