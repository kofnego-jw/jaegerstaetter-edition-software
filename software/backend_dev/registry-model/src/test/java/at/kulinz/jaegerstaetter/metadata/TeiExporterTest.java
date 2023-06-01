package at.kulinz.jaegerstaetter.metadata;

import at.kulinz.jaegerstaetter.metadata.registry.repository.JsonFileRepository;
import at.kulinz.jaegerstaetter.metadata.registry.repository.TeiExporter;
import at.kulinz.jaegerstaetter.metadata.registry.repository.TeiExporterImpl;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class TeiExporterTest {

    private static final File REPO_FILE = new File("../registry/workingDir/repository/data.json");
    private static final File XML_FILE = new File("src/test/resources/jsonRepo.xml");

    @Test
    public void createXmlFile() throws Exception {
        Assumptions.assumeTrue(REPO_FILE.exists());
        JsonFileRepository jsonFileRepository = new JsonFileRepository(REPO_FILE);
        XmlMapper xmlMapper = new XmlMapper();
        byte[] content = xmlMapper.writeValueAsBytes(jsonFileRepository.getJsonRepository());
        FileUtils.writeByteArrayToFile(XML_FILE, content);
    }

    @Test
    public void convertToTei() throws Exception {
        TeiExporter exporter = new TeiExporterImpl();
        JsonFileRepository jsonFileRepository = new JsonFileRepository(REPO_FILE);
        byte[] content = exporter.exportToTei(jsonFileRepository);
        Assertions.assertNotNull(content);
        Assertions.assertNotEquals(0, content.length);
        System.out.println(new String(content, StandardCharsets.UTF_8));
    }
}
