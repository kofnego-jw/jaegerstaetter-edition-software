package at.kulinz.jaegerstaetter.metadata.authority.geonames;

import at.kulinz.jaegerstaetter.metadata.authority.TestBase;
import at.kulinz.jaegerstaetter.metadata.authority.model.Authority;
import at.kulinz.jaegerstaetter.metadata.authority.model.ControlledVocabulary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

public class GeonamesQueryServiceTest extends TestBase {

    @Autowired
    private GeonamesQueryService queryService;

    @Test
    public void test() throws Exception {
        List<GeonamesResult> results = queryService.search("St. Radegund", "de", 0);
        results.forEach(System.out::println);
    }

    @Test
    public void testGeoLocationSearch() throws Exception {
        ControlledVocabulary voc = new ControlledVocabulary(Authority.GEONAMES, "2772400", Collections.emptyList(), "");
        System.out.println(queryService.getLocationById(voc));
    }
}
