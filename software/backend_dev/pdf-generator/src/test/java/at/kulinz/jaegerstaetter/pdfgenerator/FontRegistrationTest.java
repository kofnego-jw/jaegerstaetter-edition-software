package at.kulinz.jaegerstaetter.pdfgenerator;

import at.kulinz.jaegerstaetter.pdfgenerator.service.PdfGeneratorConstants;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class FontRegistrationTest {

    private static final File FONTS_DIR = new File("src/main/resources/fonts");
    private static final File SAMPLE_HTML = new File("src/test/resources/html/sample.html");
    private static final File GENERATE_OUPUT = new File("src/test/resources/html/sample.pdf");

    @Test
    public void test() throws Exception {
        File[] fontFiles = FONTS_DIR.listFiles(x -> x.getName().endsWith(".ttf"));
        PdfRendererBuilder builder = new PdfRendererBuilder();
        Assertions.assertNotNull(fontFiles);
        for (File fontFile : fontFiles) {
            Assertions.assertDoesNotThrow(() -> PdfGeneratorConstants.registerFont(fontFile, builder));
        }
        DocumentBuilder db = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder();
        Document html = db.parse(SAMPLE_HTML);
        try (OutputStream os = new FileOutputStream(GENERATE_OUPUT)) {
            builder.withW3cDocument(html, SAMPLE_HTML.getParentFile().toURI().toString());
            builder.toStream(os);
            builder.run();
        }
    }
}
