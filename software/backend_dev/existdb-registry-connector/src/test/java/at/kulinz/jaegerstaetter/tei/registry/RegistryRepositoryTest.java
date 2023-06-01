package at.kulinz.jaegerstaetter.tei.registry;

import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiNotVersionedRepository;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.tei.registry.service.RegistryRepository;
import at.kulinz.jaegerstaetter.testhelper.service.ExistConnectionTestHelper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class RegistryRepositoryTest extends TestBase {

    @Autowired
    private RegistryRepository metadataRegistryRepository;

    @Autowired
    private ExistConnectionTestHelper testHelper;

    @Autowired
    private ExistDBTeiNotVersionedRepository ingestRepository;

    @BeforeEach
    public void setupExistRepository() {
        Assumptions.assumeTrue(testHelper.connectionAvailable());
    }

    @AfterEach
    public void deleteTestRepo() throws Exception {
        if (testHelper.connectionAvailable()) {
            ingestRepository.deleteAll();
        }
    }

    @Test
    public void test() {
        Optional<TeiDocument> v0Opt = metadataRegistryRepository.getIngestRegistryXmlDocument();
        Assumptions.assumeTrue(v0Opt.isEmpty());
        Assertions.assertDoesNotThrow(() -> {
            metadataRegistryRepository.updateRegistryToIngest("<v1/>".getBytes(StandardCharsets.UTF_8), "{\"version\":1}".getBytes(StandardCharsets.UTF_8));
        });
        Optional<TeiDocument> v1Opt = metadataRegistryRepository.getIngestRegistryXmlDocument();
        Assertions.assertTrue(v1Opt.isPresent());
        Assertions.assertArrayEquals("<v1/>".getBytes(StandardCharsets.UTF_8), v1Opt.get().getContent());
        Assertions.assertDoesNotThrow(() -> {
            metadataRegistryRepository.updateRegistryToIngest("<v2/>".getBytes(StandardCharsets.UTF_8), "{\"version\":2}".getBytes(StandardCharsets.UTF_8));
        });
        Optional<TeiDocument> v2Opt = metadataRegistryRepository.getIngestRegistryXmlDocument();
        Assertions.assertTrue(v2Opt.isPresent());
        Assertions.assertArrayEquals("<v2/>".getBytes(StandardCharsets.UTF_8), v2Opt.get().getContent());
        Assertions.assertEquals(0, metadataRegistryRepository.getEditionRegistryXmlDocumentVersions().size());


    }
}
