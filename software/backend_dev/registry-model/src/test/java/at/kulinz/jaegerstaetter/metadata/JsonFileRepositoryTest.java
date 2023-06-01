package at.kulinz.jaegerstaetter.metadata;

import at.kulinz.jaegerstaetter.metadata.authority.model.Authority;
import at.kulinz.jaegerstaetter.metadata.authority.model.ControlledVocabulary;
import at.kulinz.jaegerstaetter.metadata.authority.model.GeoLocation;
import at.kulinz.jaegerstaetter.metadata.authority.model.Sex;
import at.kulinz.jaegerstaetter.metadata.registry.MetadataException;
import at.kulinz.jaegerstaetter.metadata.registry.model.PersonInfo;
import at.kulinz.jaegerstaetter.metadata.registry.model.PlaceInfo;
import at.kulinz.jaegerstaetter.metadata.registry.repository.JsonFileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class JsonFileRepositoryTest extends TestBase {

    private static final PersonInfo SAMPLE_PERSON_INFO = new PersonInfo("key", "prefName", "surname",
            "forename", List.of("add1", "add2"), List.of("role1", "role2"), "1900-01-01", "Birth Place",
            List.of("Residence"), "1980-12-31", "Death Place", Sex.MALE, "Austria", "note",
            "internal notes",
            List.of(new ControlledVocabulary(Authority.GND, "1", List.of("Test", "test"), "name")),
            null, null);

    private static final PlaceInfo SAMPLE_PLACE_INFO = new PlaceInfo("Nirgendwo", "region", "nirgendwo",
            "Name gut", "notes", "todo",
            List.of(new ControlledVocabulary(Authority.GEONAMES, "1234", List.of("Nirgendwo", "Nirgends"), "hello")),
            new GeoLocation(45D, 13D), null);

    private JsonFileRepository jsonFileRepository;

    @BeforeEach
    public void createJsonDataRepository() throws Exception {
        this.jsonFileRepository = new JsonFileRepository(JSON_REPOSITORY_FILE);
    }

    @Test
    public void testFunctionalities() throws Exception {
        this.jsonFileRepository.clear();
        Assertions.assertTrue(this.jsonFileRepository.findAllPersonInfos().isEmpty());
        Assertions.assertTrue(this.jsonFileRepository.findAllPlaceInfos().isEmpty());

        this.jsonFileRepository.addPersonInfo(SAMPLE_PERSON_INFO);

        Assertions.assertThrows(MetadataException.class, () -> this.jsonFileRepository.addPersonInfo(SAMPLE_PERSON_INFO));
        Assertions.assertEquals(1, this.jsonFileRepository.findAllPersonInfos().size());
        Assertions.assertEquals("prefName", this.jsonFileRepository.findAllPersonInfos().get(0).generatedReadableName);
        Assertions.assertEquals("prefName", this.jsonFileRepository.findAllPersonInfos().get(0).getGeneratedOfficialName());
        Assertions.assertEquals(0, this.jsonFileRepository.findAllPlaceInfos().size());
        Assertions.assertDoesNotThrow(() -> this.jsonFileRepository.addPlaceInfo(SAMPLE_PLACE_INFO));
        Assertions.assertThrows(MetadataException.class, () -> this.jsonFileRepository.addPlaceInfo(SAMPLE_PLACE_INFO));
        Assertions.assertEquals(1, this.jsonFileRepository.findAllPlaceInfos().size());

        Assertions.assertDoesNotThrow(() -> this.jsonFileRepository.removePersonInfoByKey(SAMPLE_PERSON_INFO.key));
        Assertions.assertDoesNotThrow(() -> this.jsonFileRepository.removePersonInfoByKey(SAMPLE_PERSON_INFO.key));
        Assertions.assertDoesNotThrow(() -> this.jsonFileRepository.removePlaceInfoByKey(SAMPLE_PLACE_INFO.key));
    }

    @Test
    public void testReadability() throws Exception {
        this.jsonFileRepository.addPersonInfo(SAMPLE_PERSON_INFO);
        this.jsonFileRepository.addPlaceInfo(SAMPLE_PLACE_INFO);
        Assertions.assertDoesNotThrow(() -> new JsonFileRepository(JSON_REPOSITORY_FILE));
    }
}
