package at.kulinz.jaegerstaetter.tei.edition.test;

import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.stylesheets.model.MetadataResult;
import at.kulinz.jaegerstaetter.tei.edition.connector.impl.MetadataService;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.stream.Stream;

public class MetadataServiceTest extends TestBaseMetadataService {

    public static final File DATA = new File("../../../daten/XML_Briefe, Vorlage&Index");

    @Autowired
    private MetadataService metadataService;

    public static TeiDocument mock(File file) {
        try {
            return new TeiDocument(file.getName(), 1, file.getName(), LocalDateTime.now(), null, FileUtils.readFileToByteArray(file));
        } catch (Exception e) {
            throw new RuntimeException("Error", e);
        }
    }

    @Test
    public void test() throws Exception {
        Assertions.assertNotNull(this.metadataService);
        Stream.of(DATA.listFiles(x -> x.getName().endsWith(".xml")))
                .sorted(Comparator.comparing(File::getName))
                .forEach(f -> {
                    try {
                        MetadataResult result = metadataService.getMetadataResult(mock(f));
                        System.out.println(f.getName() + ": " + result.title + " dating: " + result.dating);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
