package at.kulinz.jaegerstaetter.metadata.authority.wikidata;

import at.kulinz.jaegerstaetter.metadata.authority.TestBase;
import at.kulinz.jaegerstaetter.metadata.authority.model.ControlledVocabulary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class WikidataQueryServiceTest extends TestBase {

    @Autowired
    private WikidataQueryService queryService;

    @Test
    public void test_person() throws Exception {
        List<ControlledVocabulary> results = this.queryService.query("Franz Jägerstätter", "de", 0);
        results.forEach(System.out::println);
    }

    @Test
    public void test_place() throws Exception {
        List<ControlledVocabulary> results = this.queryService.query("Berlin", "de", 0);
        results.forEach(System.out::println);
    }

    @Test
    public void test_keyword() throws Exception {
        List<ControlledVocabulary> results = this.queryService.query("Religion", "de", 0);
        results.forEach(System.out::println);
    }
}
