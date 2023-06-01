package at.kulinz.jaegerstaetter.xsltstylesheets.model;

import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.*;
import at.kulinz.jaegerstaetter.stylesheets.model.AttributeDesc;
import at.kulinz.jaegerstaetter.stylesheets.model.DocDescResult;
import at.kulinz.jaegerstaetter.stylesheets.model.ElementDesc;
import at.kulinz.jaegerstaetter.stylesheets.model.MetadataResult;
import at.kulinz.jaegerstaetter.stylesheets.service.JacksonXmlHelper;
import at.kulinz.jaegerstaetter.xsltstylesheets.TestConstants;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class XmlSampleFileCreationTest {

    @Test
    public void testCreateVersionInfoXml() throws Exception {
        VersionInfo one = new VersionInfo(1, "2023-04-05T10:22");
        VersionInfo two = new VersionInfo(2, "2023-04-07T10:22");
        VersionInfoList list = new VersionInfoList(List.of(two, one));
        String xml = JacksonXmlHelper.toXML(list);
        System.out.println(xml.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
    }

    @Test
    public void createSampleIndexDocumentFile() throws Exception {
        String docId = "B1.11.xml";
        String sourceId = "B1.11.xml";
        IndexField f1 = new IndexField(IndexFieldname.ALL, List.of("one", "two"));
        IndexField f2 = new IndexField(IndexFieldname.TRANSCRIPT, List.of("one"));
        IndexField f3 = new IndexField(IndexFieldname.COMMENTARY, List.of("two"));
        List<IndexField> fields = List.of(f1, f2, f3);
        IndexDocument indexDocument = new IndexDocument(docId, sourceId, fields);

        String xml = JacksonXmlHelper.toXML(indexDocument);
        FileUtils.writeStringToFile(TestConstants.SAMPLE_INDEX_DOCUMENT_XML, xml, StandardCharsets.UTF_8);

        IndexDocument deserialized = JacksonXmlHelper.fromInputStreamToIndexDocument(
                new FileInputStream((TestConstants.SAMPLE_INDEX_DOCUMENT_XML)));
        Assertions.assertEquals(3, deserialized.fields.size());
    }

    @Test
    public void createSampleMetadataResultFile() throws Exception {
        String id = "B1.11.xml";
        String title = "title";
        String dating = "1912-03-15";
        ResourceType type = ResourceType.LETTER;
        List<String> facs = List.of("b.1.11.1.jpg", "b.1.11.2.jpg");
        MetadataRecord correspSenders = new MetadataRecord("Absender:innen", "Jaegerstaetter, Franz / Jaegerstaetter, Franziska", Collections.emptyList());
        MetadataRecord correspSendDate = new MetadataRecord("Absendedatum", "1912-03-15", Collections.emptyList());
        MetadataRecord correspSendPlace = new MetadataRecord("Absendeort", "St. Radegund", Collections.emptyList());
        MetadataRecord correspRecipients = new MetadataRecord("Empfänger:innen", "Karobath, Josef", Collections.emptyList());
        MetadataRecord correspReciPlace = new MetadataRecord("Empfangsort", "Linz", Collections.emptyList());
        MetadataGroup correspDescGroup = new MetadataGroup("corrspDesc",
                List.of(correspSenders, correspSendDate, correspSendPlace, correspRecipients, correspReciPlace));

        MetadataRecord msInst = new MetadataRecord("Institution", "Archiv der Diözese Linz", Collections.emptyList());
        MetadataRecord msRepo = new MetadataRecord("Repositorium", "Nachlass Franz Jägerstätter", Collections.emptyList());
        MetadataRecord msColl = new MetadataRecord("Collection", "Schenkung Haus Jägerstätter", Collections.emptyList());
        MetadataRecord msIdno = new MetadataRecord("Signatur", "B1.11", Collections.emptyList());
        MetadataRecord msAltId = new MetadataRecord("Alte Signatur", "18", Collections.emptyList());
        MetadataGroup msIdentifier = new MetadataGroup("msIdentifier",
                List.of(msInst, msRepo, msColl, msIdno, msAltId));

        MetadataRecord summary = new MetadataRecord("Zusammenfassung", "Franz Jägerstätter schickt Grüße aus Wolfern, wo er Pfarrer Josef Karobath besuchte, der ebenfalls einige Grußworte an die Familie verfasst.", Collections.emptyList());
        MetadataGroup content = new MetadataGroup("content", List.of(summary));
        List<MetadataGroup> groups = List.of(correspDescGroup, msIdentifier, content);
        MetadataResult result = new MetadataResult(id, title, dating, "B1.11", "18", type, facs, groups, "summary", Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList());


        String xml = JacksonXmlHelper.toXML(result);
        FileUtils.writeStringToFile(TestConstants.SAMPLE_METADATA_RESULT_XML, xml, StandardCharsets.UTF_8);

        MetadataResult deserialized = JacksonXmlHelper.fromInputStreamToMetadataResult(new FileInputStream((TestConstants.SAMPLE_METADATA_RESULT_XML)));
        Assertions.assertEquals(dating, deserialized.dating);
        Assertions.assertEquals(3, deserialized.metadataGroups.size());
        MetadataGroup g0 = deserialized.metadataGroups.get(0);
        Assertions.assertEquals("Absendedatum", g0.records.get(1).fieldname);
    }

    @Test
    public void createSampleDocDesc() throws Exception {
        List<AttributeDesc> refAttrs = List.of(
                new AttributeDesc("target", List.of("t1.xml", "t2.xml")),
                new AttributeDesc("n", List.of("#a", "#b"))
        );
        List<AttributeDesc> fwAttrs = List.of(
                new AttributeDesc("hand", List.of("#fw1", "#fw2"))
        );
        List<ElementDesc> allDesc = List.of(
                new ElementDesc("fw", fwAttrs),
                new ElementDesc("ref", refAttrs)
        );
        DocDescResult result = new DocDescResult("one.xml", allDesc, allDesc, allDesc, List.of("t1.xml", "t2.xml"), 1, 2, 3, 4);
        String xml = JacksonXmlHelper.toXML(result);
        FileUtils.writeStringToFile(TestConstants.SAMPLE_DOC_DESC_FILE, xml, StandardCharsets.UTF_8);

        DocDescResult deserialized = JacksonXmlHelper.fromInputStreamToDocDescResult(new FileInputStream(TestConstants.SAMPLE_DOC_DESC_FILE));
        Assertions.assertEquals(result.filename, deserialized.filename);
        Assertions.assertEquals(result.allElementDescs.size(), deserialized.allElementDescs.size());

    }
}
