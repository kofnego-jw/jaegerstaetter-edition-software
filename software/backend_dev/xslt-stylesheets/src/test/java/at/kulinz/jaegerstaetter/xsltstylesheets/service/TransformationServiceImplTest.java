package at.kulinz.jaegerstaetter.xsltstylesheets.service;

import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.IndexDocument;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.TocEntry;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.TocList;
import at.kulinz.jaegerstaetter.stylesheets.model.MetadataResult;
import at.kulinz.jaegerstaetter.stylesheets.service.JacksonXmlHelper;
import at.kulinz.jaegerstaetter.stylesheets.service.TransformationService;
import at.kulinz.jaegerstaetter.xsltstylesheets.TestBase;
import at.kulinz.jaegerstaetter.xsltstylesheets.TestConstants;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class TransformationServiceImplTest extends TestBase {


    @Autowired
    private TransformationService service;

    @Test
    public void testMetadata() throws Exception {
        Assumptions.assumeTrue(TestConstants.TEI_SAMPLE_FILE.exists());
        TeiDocument doc = new TeiDocument(TestConstants.TEI_SAMPLE_FILE.getName(), FileUtils.readFileToByteArray(TestConstants.TEI_SAMPLE_FILE),
                LocalDateTime.now());
        MetadataResult metadata = service.getMetadataResult(doc);
        // System.out.println(metadata.metadataGroups);
        Assertions.assertEquals(5, metadata.metadataGroups.size());

        Assertions.assertDoesNotThrow(() -> {
            service.toNormalized(doc);
        });
        Assertions.assertDoesNotThrow(() -> {
            service.toDiplomatic(doc);
        });
    }

    @Test
    public void testLucene() throws Exception {
        Assumptions.assumeTrue(TestConstants.TEI_SAMPLE_FILE.exists());
        TeiDocument doc = new TeiDocument(TestConstants.TEI_SAMPLE_FILE.getName(), FileUtils.readFileToByteArray(TestConstants.TEI_SAMPLE_FILE),
                LocalDateTime.now());
        IndexDocument indexDocument = service.getIndexDocument(doc, null);
        Assertions.assertEquals(8, indexDocument.fields.size());
        Assertions.assertEquals(TestConstants.TEI_SAMPLE_FILE.getName(), indexDocument.documentId);
    }

    @Test
    public void createSampleTocEntryFile() throws Exception {
        List<TocEntry> children11 = List.of(
                new TocEntry("id1_1_1", "title 1.1.1", Collections.emptyList()),
                new TocEntry("id1_1_2", "title 1.1.2", Collections.emptyList()));
        List<TocEntry> children1 = List.of(new TocEntry("id1_1", "title 1.1", children11),
                new TocEntry("id1_2", "title 1.2", Collections.emptyList()));
        List<TocEntry> toc = List.of(new TocEntry("id1", "title1", children1),
                new TocEntry("id2", "title2", null));
        String xml = JacksonXmlHelper.toXML(new TocList(toc));
        File out = new File("src/test/resources/samples/tocListSample.xml");
        FileUtils.writeStringToFile(out, xml, StandardCharsets.UTF_8);
    }
}
