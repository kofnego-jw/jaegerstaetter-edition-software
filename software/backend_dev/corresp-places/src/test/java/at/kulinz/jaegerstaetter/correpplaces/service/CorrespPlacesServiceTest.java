package at.kulinz.jaegerstaetter.correpplaces.service;

import at.kulinz.jaegerstaetter.correpplaces.TestBase;
import at.kulinz.jaegerstaetter.correspplaces.service.CorrespPlacesService;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.CorrespInfo;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.DocumentInfo;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Optional;

public class CorrespPlacesServiceTest extends TestBase {

    private static final File DATA_DIR = new File("../../../daten/XML_Briefe, Vorlage&Index");

    @Autowired
    private CorrespPlacesService previewCorrespPlacesService;

    @Test
    public void test() throws Exception {
        previewCorrespPlacesService.reset();
        for (File f : DATA_DIR.listFiles(x -> x.getName().endsWith(".xml"))) {
            previewCorrespPlacesService.analyzeDocument(mockTeiDocument(f));
        }
        previewCorrespPlacesService.getAllCorresPlaces().forEach(System.out::println);
        Optional<CorrespInfo> infoOpt = previewCorrespPlacesService.findByDocumentInfo(new DocumentInfo("L1_H2.xml", "L2_10_a_xml_Es_war"));
        Assertions.assertTrue(infoOpt.isPresent());
        Assertions.assertEquals(3, infoOpt.get().places.size());
    }

    private TeiDocument mockTeiDocument(File f) throws Exception {
        return new TeiDocument(f.getParentFile().getName() + "/" + f.getName(), FileUtils.readFileToByteArray(f), LocalDateTime.now());
    }

}
