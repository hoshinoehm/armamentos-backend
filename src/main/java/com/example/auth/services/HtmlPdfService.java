package com.example.auth.services;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.util.XRLog;
import jakarta.annotation.PostConstruct;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Serviço responsável por renderizar templates Thymeleaf em PDF
 * usando OpenHTMLToPDF + PDFBox.
 *
 * <p>Desabilita os logs verbosos do OpenHTMLToPDF na inicialização.</p>
 */
@Service
public class HtmlPdfService {

    private final ITemplateEngine templateEngine;

    /** URI base usada pelo OpenHTMLToPDF para resolver imagens do cabeçalho. */
    private String reportsBaseUri;

    public HtmlPdfService(ITemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     * Extrai as imagens do classpath para um diretório temporário e registra
     * a URI base. Funciona tanto em desenvolvimento (classpath file:) quanto
     * em fat JARs (classpath jar:).
     */
    @PostConstruct
    private void initBaseUri() throws Exception {
        XRLog.setLoggingEnabled(false);

        Path tempDir = Files.createTempDirectory("armamentos-reports");
        Path imgDir = tempDir.resolve("img");
        Files.createDirectories(imgDir);

        for (String img : new String[]{"logo_pmma.png", "logo_institucional.png", "brasao_ma.png"}) {
            try (InputStream is = getClass().getResourceAsStream("/reports/img/" + img)) {
                if (is != null) {
                    Files.copy(is, imgDir.resolve(img), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
        String uri = tempDir.toUri().toString();
        reportsBaseUri = uri.endsWith("/") ? uri : uri + "/";
    }

    /**
     * Renderiza um template Thymeleaf em bytes PDF.
     *
     * @param templateName nome do template (sem extensão), dentro de {@code classpath:/templates/}
     * @param ctx          contexto Thymeleaf com as variáveis do template
     * @return PDF em bytes
     */
    public byte[] render(String templateName, Context ctx) throws Exception {
        // 1. Renderiza o template Thymeleaf → HTML String
        String html = templateEngine.process(templateName, ctx);

        // 2. Jsoup faz o parse tolerante e converte para XML bem formado
        Document jsoupDoc = Jsoup.parse(html, "UTF-8");
        jsoupDoc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // 3. OpenHTMLToPDF converte XML → PDF
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useFastMode();
        builder.withW3cDocument(w3cDoc, reportsBaseUri);
        builder.toStream(baos);
        builder.run();

        return baos.toByteArray();
    }

    /**
     * Mescla múltiplos PDFs em um único PDF usando PDFBox.
     *
     * @param pdfs bytes de cada PDF a mesclar, na ordem desejada
     * @return PDF resultante em bytes
     */
    public byte[] merge(byte[]... pdfs) throws Exception {
        PDFMergerUtility merger = new PDFMergerUtility();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        merger.setDestinationStream(out);
        for (byte[] pdf : pdfs) {
            merger.addSource(new ByteArrayInputStream(pdf));
        }
        merger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
        return out.toByteArray();
    }
}
