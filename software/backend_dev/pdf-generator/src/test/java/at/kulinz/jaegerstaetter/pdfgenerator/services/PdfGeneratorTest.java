package at.kulinz.jaegerstaetter.pdfgenerator.services;

import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.pdfgenerator.TestBase;
import at.kulinz.jaegerstaetter.pdfgenerator.service.PdfGenerator;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.time.LocalDateTime;

public class PdfGeneratorTest extends TestBase {

    private static final File DATA_DIR = new File("../../../daten/XML_Briefe, Vorlage&Index");
    private static final File BIOGRAPHY_DIR = new File("../../../daten/Biografien");

    private static final File OUTPUT_DIR = new File("target/testpdf/");

    @Autowired
    private PdfGenerator pdfGenerator;

    @Autowired
    private String portNumber;

    private static TeiDocument mockTeiDocument(File f) throws Exception {
        return new TeiDocument(f.getName(), FileUtils.readFileToByteArray(f), LocalDateTime.now());
    }

    @BeforeEach
    public void checkInternalConnection() throws Exception {
        int port = Integer.parseInt(this.portNumber);
        SocketAddress address = new InetSocketAddress(port);
        try (Socket socket = new Socket()) {
            socket.connect(address);
        } catch (Exception e) {
            Assumptions.assumeTrue(false);
        }
    }

    @Test
    public void test_generateBioPdf() throws Exception {
        Assumptions.assumeTrue(BIOGRAPHY_DIR.exists());
        if (!OUTPUT_DIR.exists() && !OUTPUT_DIR.mkdirs()) {
            throw new Exception("Cannot create output directory");
        }
        for (File f : BIOGRAPHY_DIR.listFiles(x -> x.getName().endsWith(".xml"))) {
            byte[] pdfBytes = pdfGenerator.generateBiographyPdf(mockTeiDocument(f));
            File out = new File(OUTPUT_DIR, f.getName() + ".pdf");
            FileUtils.writeByteArrayToFile(out, pdfBytes);
        }
    }

    //@Test
    public void test() throws Exception {
        Assumptions.assumeTrue(DATA_DIR.exists());
        if (!OUTPUT_DIR.exists() && !OUTPUT_DIR.mkdirs()) {
            throw new Exception("Cannot create output directory");
        }
        for (File f : DATA_DIR.listFiles(x -> x.getName().endsWith(".xml"))) {
            byte[] pdfBytes = pdfGenerator.generatePdf(mockTeiDocument(f));
            File out = new File(OUTPUT_DIR, f.getName() + ".pdf");
            FileUtils.writeByteArrayToFile(out, pdfBytes);
        }
    }
}
