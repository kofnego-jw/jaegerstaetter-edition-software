package at.kulinz.jaegerstaetter.pdfgenerator.service;

import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.stylesheets.service.TransformationService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

@Component
public class PdfGeneratorImpl implements PdfGenerator {

    private final TransformationService transformationService;
    private final DocumentBuilder documentBuilder;
    private final File fontsDir;
    private final Integer portNumber;

    @Autowired
    public PdfGeneratorImpl(TransformationService transformationService, File fontsDir, String portNumber) throws Exception {
        this.transformationService = transformationService;
        this.fontsDir = fontsDir;
        this.portNumber = Integer.parseInt(portNumber);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setNamespaceAware(true);
        this.documentBuilder = dbFactory.newDocumentBuilder();
        this.initializeFontsDir();
    }

    private void initializeFontsDir() throws Exception {
        if (!fontsDir.exists() && !fontsDir.mkdirs()) {
            throw new Exception("Cannot recreate fonts directory.");
        }
        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] fontResources = patternResolver.getResources(PdfGeneratorConstants.FONTS_RESOURCES_PATTERN);
        for (Resource r : fontResources) {
            if (!r.isReadable()) {
                continue;
            }
            String filename = r.getFilename();
            if (filename == null) {
                continue;
            }
            File output = new File(fontsDir, filename);
            FileUtils.copyInputStreamToFile(r.getInputStream(), output);
        }
    }

    private Document generateW3Document(TeiDocument document) throws Exception {
        byte[] bytes = transformationService.toPrintHtml(document);
        return documentBuilder.parse(new ByteArrayInputStream(bytes));
    }

    private Document generateBiographyW3Document(TeiDocument document) throws Exception {
        byte[] bytes = transformationService.biographyToPrintHtml(document, portNumber);
        return documentBuilder.parse(new ByteArrayInputStream(bytes));
    }

    private void registerFonts(PdfRendererBuilder builder) {
        File[] fonts = fontsDir.listFiles(x -> x.getName().endsWith(".ttf"));
        if (fonts == null) {
            return;
        }
        for (File f : fonts) {
            PdfGeneratorConstants.registerFont(f, builder);
        }
    }

    @Override
    public byte[] generatePdf(TeiDocument teiDocument) throws Exception {
        PdfRendererBuilder builder = new PdfRendererBuilder();
        Document document = generateW3Document(teiDocument);
        registerFonts(builder);
        builder.useFastMode();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        builder.withW3cDocument(document, fontsDir.toURI().toString());
        builder.toStream(baos);
        builder.run();
        return baos.toByteArray();
    }

    @Override
    public byte[] generateBiographyPdf(TeiDocument teiDocument) throws Exception {
        PdfRendererBuilder builder = new PdfRendererBuilder();
        Document document = generateBiographyW3Document(teiDocument);
        registerFonts(builder);
        builder.useFastMode();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        builder.withW3cDocument(document, fontsDir.toURI().toString());
        builder.toStream(baos);
        builder.run();
        return baos.toByteArray();
    }
}
