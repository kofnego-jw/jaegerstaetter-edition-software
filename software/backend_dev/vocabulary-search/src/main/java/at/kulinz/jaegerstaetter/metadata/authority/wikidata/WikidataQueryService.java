package at.kulinz.jaegerstaetter.metadata.authority.wikidata;

import at.kulinz.jaegerstaetter.metadata.authority.AuthorityException;
import at.kulinz.jaegerstaetter.metadata.authority.model.Authority;
import at.kulinz.jaegerstaetter.metadata.authority.model.ControlledVocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.wikidata.wdtk.wikibaseapi.ApiConnection;
import org.wikidata.wdtk.wikibaseapi.BasicApiConnection;
import org.wikidata.wdtk.wikibaseapi.WbSearchEntitiesAction;
import org.wikidata.wdtk.wikibaseapi.WbSearchEntitiesResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WikidataQueryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WikidataQueryService.class);

    private static final long DEFAULT_RESULT_LIMIT = 50L;

    private final WbSearchEntitiesAction action;

    public WikidataQueryService() {
        ApiConnection con = new BasicApiConnection(ApiConnection.URL_WIKIDATA_API);
        action = new WbSearchEntitiesAction(con, "http://wikidata.org/entity");
    }

    private static List<ControlledVocabulary> convertResultToControlledVocabularies(
            List<WbSearchEntitiesResult> results) {
        return results.stream().map(WikidataQueryService::toControlledVocabulary)
                .collect(Collectors.toList());
    }

    private static ControlledVocabulary toControlledVocabulary(WbSearchEntitiesResult result) {
        String id = result.getEntityId();
        String title = result.getLabel();
        List<String> names = result.getAliases() == null ? Collections.emptyList() :
                new ArrayList<>(result.getAliases());
        List<String> titles = new ArrayList<>();
        titles.add(title);
        titles.addAll(names);
        return new ControlledVocabulary(Authority.WIKIDATA, id, titles, title);
    }

    public List<ControlledVocabulary> query(String searchTerm, String language, long page) throws AuthorityException {
        LOGGER.info("Wikidata Query for: {} in {} page {}.", searchTerm, language, page);
        try {
            return convertResultToControlledVocabularies(
                    action.wbSearchEntities(searchTerm, language, false, "item",
                            DEFAULT_RESULT_LIMIT, page * DEFAULT_RESULT_LIMIT));

        } catch (Exception e) {
            LOGGER.error("Cannot perform wikidata query.", e);
            throw new AuthorityException("Cannot perform wikidata query.", e);
        }
    }

}
