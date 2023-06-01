package at.kulinz.jaegerstaetter.metadata.authority.geonames;

import at.kulinz.jaegerstaetter.metadata.authority.AuthorityException;
import at.kulinz.jaegerstaetter.metadata.authority.model.Authority;
import at.kulinz.jaegerstaetter.metadata.authority.model.ControlledVocabulary;
import at.kulinz.jaegerstaetter.metadata.authority.model.GeoLocation;
import org.geonames.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class GeonamesQueryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeonamesQueryService.class);

    private static final String GEONAMES_USERNAME = "joseph_wang";

    private static final int DEFAULT_PAGE_SIZE = 50;

    public GeonamesQueryService() {
        WebService.setUserName(GEONAMES_USERNAME);
    }

    private static List<GeonamesResult> convertToControlledPlaceVocabularies(ToponymSearchResult result) {
        if (result == null) {
            return Collections.emptyList();
        }
        return result.getToponyms().stream()
                .map(GeonamesQueryService::convertToControlledPlaceVocabulary)
                .collect(Collectors.toList());
    }

    private static GeonamesResult convertToControlledPlaceVocabulary(Toponym toponym) {
        GeoLocation coordinates = new GeoLocation(toponym.getLatitude(), toponym.getLongitude());
        Authority authority = Authority.GEONAMES;
        String id = Integer.toString(toponym.getGeoNameId());
        String title = toponym.getName();
        List<String> names;
        try {
            names = Stream.of(toponym.getAlternateNames().split(",")).collect(Collectors.toList());
        } catch (Exception ignored) {
            names = Collections.emptyList();
        }
        List<String> titles = new ArrayList<>();
        titles.add(title);
        titles.addAll(names);
        return new GeonamesResult(new ControlledVocabulary(authority, id, titles, title), coordinates);
    }

    public List<GeonamesResult> search(String queryString, String language,
                                       int pageNumber) throws AuthorityException {
        int startRow = pageNumber * DEFAULT_PAGE_SIZE;
        ToponymSearchCriteria criteria = new ToponymSearchCriteria();
        criteria.setQ(queryString);
        criteria.setStyle(Style.FULL);
        criteria.setStartRow(startRow);
        criteria.setLanguage(language);
        LOGGER.info("Query Geonames for {} in {} page {}.", queryString, language, pageNumber);
        try {
            ToponymSearchResult result = WebService.search(criteria);
            return convertToControlledPlaceVocabularies(result);
        } catch (Exception e) {
            LOGGER.error("Cannot perform geonames search.", e);
            throw new AuthorityException("Cannot perform geonames search.", e);
        }
    }

    public GeonamesResult findRecordById(String controlledId) throws AuthorityException {
        try {
            int id = Integer.parseInt(controlledId);
            Toponym toponym = WebService.get(id, "de", null);
            return convertToControlledPlaceVocabulary(toponym);
        } catch (Exception e) {
            throw new AuthorityException("Cannot perform search.", e);
        }
    }

    public GeoLocation getLocationById(ControlledVocabulary voc) throws AuthorityException {
        if (voc.authority != Authority.GEONAMES) {
            throw new AuthorityException("Cannot find geolocation without geonames id.");
        }
        String controlledId = voc.controlledId;
        try {
            int id = Integer.parseInt(controlledId);
            Toponym toponym = WebService.get(id, "de", null);
            return new GeoLocation(toponym.getLatitude(), toponym.getLongitude());
        } catch (Exception e) {
            throw new AuthorityException("Cannot perform search.", e);
        }
    }


}
