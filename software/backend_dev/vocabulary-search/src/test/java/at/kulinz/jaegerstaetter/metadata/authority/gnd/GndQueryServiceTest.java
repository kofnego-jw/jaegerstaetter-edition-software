package at.kulinz.jaegerstaetter.metadata.authority.gnd;

import at.kulinz.jaegerstaetter.metadata.authority.TestBase;
import at.kulinz.jaegerstaetter.metadata.authority.model.Authority;
import at.kulinz.jaegerstaetter.metadata.authority.model.ControlledVocabulary;
import at.kulinz.jaegerstaetter.metadata.authority.model.VocabularyType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

public class GndQueryServiceTest extends TestBase {

    @Autowired
    private GndQueryService queryService;


    @Test
    public void test_person() throws Exception {
        List<GndRecord> result = queryService.query("Jägerstätter", 0, VocabularyType.PERSON);
        result.forEach(System.out::println);
    }

    @Test
    public void test_place() throws Exception {
        List<GndRecord> result = queryService.query("Berlin", 0, VocabularyType.PLACE);
        result.forEach(System.out::println);
    }

    @Test
    public void test_keyword() throws Exception {
        List<GndRecord> result = queryService.query("Theologie", 0, VocabularyType.KEYWORD);
        result.forEach(System.out::println);
    }

    @Test
    public void test_corporation() throws Exception {
        List<GndRecord> result = queryService.query("Bischöfliches Ordinariat Linz", 0, VocabularyType.CORPORATION);
        result.forEach(System.out::println);
    }

    @Test
    public void test_saint() throws Exception {
        List<GndRecord> result = queryService.query("Augustinus", 0, VocabularyType.SAINT);
        result.forEach(System.out::println);
    }

    @Test
    public void test_geoLocationSearch() throws Exception {
        ControlledVocabulary voc = new ControlledVocabulary(Authority.GND, "4074255-6", Collections.emptyList(), "");
        System.out.println(queryService.getLocationById(voc));
    }
}
