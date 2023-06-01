package at.kulinz.jaegerstaetter.metadata;

import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiRepository;
import at.kulinz.jaegerstaetter.metadata.authority.model.Authority;
import at.kulinz.jaegerstaetter.metadata.authority.model.ControlledVocabulary;
import at.kulinz.jaegerstaetter.metadata.authority.model.GeoLocation;
import at.kulinz.jaegerstaetter.metadata.authority.model.Sex;
import at.kulinz.jaegerstaetter.metadata.registry.MetadataException;
import at.kulinz.jaegerstaetter.metadata.registry.model.PersonInfo;
import at.kulinz.jaegerstaetter.metadata.registry.model.PlaceInfo;
import at.kulinz.jaegerstaetter.metadata.registry.repository.JsonExistRepository;
import at.kulinz.jaegerstaetter.metadata.registry.repository.JsonFileRepository;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import org.junit.jupiter.api.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class JsonExistRepositoryTest extends TestBase {

    private static final PersonInfo SAMPLE_PERSON_INFO = new PersonInfo("key", "prefName", "surname",
            "forename", List.of("add1", "add2"), List.of("role1", "role2"), "1900-01-01", "Birth Place",
            List.of("Residence"), "1980-12-31", "Death Place", Sex.MALE, "Austria", "note",
            "internal notes",
            List.of(new ControlledVocabulary(Authority.GND, "1", List.of("Test", "test"), "name")), null, null);

    private static final PlaceInfo SAMPLE_PLACE_INFO = new PlaceInfo("Nirgendwo", "region", "nirgendwo",
            "Name gut", "notes", "todo",
            List.of(new ControlledVocabulary(Authority.GEONAMES, "1234", List.of("Nirgendwo", "Nirgends"), "hello")),
            new GeoLocation(45D, 13D), null);

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "";
    private static final String TESTURL = "http://localhost:8080/exist/webdav/db";
    private static final String TEST_BASE_URL = "http://localhost:8080/exist/webdav/db/test/";
    private JsonExistRepository jsonExistRepository;

    @BeforeAll
    public static void setupExistRepository() throws Exception {
        Sardine sardine = SardineFactory.begin(USERNAME, PASSWORD);
        try {
            sardine.list(TESTURL);
            // sardine.delete(TestConstants.TEST_BASE_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Exception e = null;
        try {
            sardine.createDirectory(TEST_BASE_URL);
            sardine.put(TEST_BASE_URL + "test.txt", "test".getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            e = ex;
        }
        Assumptions.assumeTrue(e == null);
    }

    //@AfterAll
    public static void deleteTestRepo() {
        Sardine sardine = SardineFactory.begin(USERNAME, PASSWORD);
        try {
            sardine.delete(TEST_BASE_URL);
        } catch (Exception e) {
            Assumptions.assumeTrue(e == null);
        }
    }

    @BeforeEach
    public void createJsonDataRepository() throws Exception {
        ExistDBTeiRepository teiRepo = new ExistDBTeiRepository("http", "localhost", 8080, "/exist/webdav/db/test/", USERNAME, PASSWORD);
        this.jsonExistRepository = new JsonExistRepository(teiRepo, "jsonrepo/data.json");
        System.out.println("JsonDataRepo created");
    }

    @Test
    public void testFunctionalities() throws Exception {
        this.jsonExistRepository.clear();
        Assertions.assertTrue(this.jsonExistRepository.findAllPersonInfos().isEmpty());
        Assertions.assertTrue(this.jsonExistRepository.findAllPlaceInfos().isEmpty());

        this.jsonExistRepository.addPersonInfo(SAMPLE_PERSON_INFO);

        Assertions.assertThrows(MetadataException.class, () -> this.jsonExistRepository.addPersonInfo(SAMPLE_PERSON_INFO));
        Assertions.assertEquals(1, this.jsonExistRepository.findAllPersonInfos().size());
        Assertions.assertEquals(0, this.jsonExistRepository.findAllPlaceInfos().size());
        Assertions.assertDoesNotThrow(() -> this.jsonExistRepository.addPlaceInfo(SAMPLE_PLACE_INFO));
        Assertions.assertThrows(MetadataException.class, () -> this.jsonExistRepository.addPlaceInfo(SAMPLE_PLACE_INFO));
        Assertions.assertEquals(1, this.jsonExistRepository.findAllPlaceInfos().size());

        Assertions.assertDoesNotThrow(() -> this.jsonExistRepository.removePersonInfoByKey(SAMPLE_PERSON_INFO.key));
        Assertions.assertDoesNotThrow(() -> this.jsonExistRepository.removePersonInfoByKey(SAMPLE_PERSON_INFO.key));
        Assertions.assertDoesNotThrow(() -> this.jsonExistRepository.removePlaceInfoByKey(SAMPLE_PLACE_INFO.key));
    }

    @Test
    public void testReadability() throws Exception {
        this.jsonExistRepository.addPersonInfo(SAMPLE_PERSON_INFO);
        this.jsonExistRepository.addPlaceInfo(SAMPLE_PLACE_INFO);
        Assertions.assertDoesNotThrow(() -> new JsonFileRepository(JSON_REPOSITORY_FILE));
    }
}
