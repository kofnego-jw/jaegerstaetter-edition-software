package at.kulinz.jaegerstaetter.metadata.authority.gnd;

import at.kulinz.jaegerstaetter.metadata.authority.AuthorityException;
import at.kulinz.jaegerstaetter.metadata.authority.model.Authority;
import at.kulinz.jaegerstaetter.metadata.authority.model.ControlledVocabulary;
import at.kulinz.jaegerstaetter.metadata.authority.model.GeoLocation;
import at.kulinz.jaegerstaetter.metadata.authority.model.VocabularyType;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class GndQueryService {

    public static final String SRU_URL = "https://services.dnb.de/sru/authorities";
    private static final Logger LOGGER = LoggerFactory.getLogger(GndQueryService.class);
    private static final List<NameValuePair> DEFAULT_PARAMS = List.of(
            new BasicNameValuePair("version", "1.1"),
            new BasicNameValuePair("operation", "searchRetrieve"),
            new BasicNameValuePair("recordSchema", "RDFxml")
    );

    private static final int DEFAULT_PAGE_SIZE = 50;

    private final GndXPathService xPathService;

    public GndQueryService() throws Exception {
        this.xPathService = new GndXPathService();
    }

    private String encodeQuery(String queryString, VocabularyType type) {
        return (switch (type) {
            case PERSON, SAINT -> "PER=" + queryString + " and BBG=Tp*";
            case PLACE -> "GEO=" + queryString + " and BBG=Tg*";
            case KEYWORD -> "WOE=" + queryString + " and BBG=Ts*";
            case CORPORATION -> "KOE=" + queryString;
        }) + " sortby tit/sort.ascending";
    }

    public List<GndRecord> query(String queryString, int page, VocabularyType type) throws AuthorityException {
        if (StringUtils.isBlank(queryString)) {
            LOGGER.warn("Cannot query empty string.");
            return Collections.emptyList();
        }
        if (type == null) {
            type = VocabularyType.PERSON;
        }
        LOGGER.info("Query GND '{}' as {}.", queryString, type);
        int startNumber = page * DEFAULT_PAGE_SIZE + 1;
        try (final CloseableHttpClient client = HttpClients.createDefault()) {
            List<NameValuePair> parameters = new ArrayList<>(DEFAULT_PARAMS);
            parameters.add(new BasicNameValuePair("maximumRecords", Integer.toString(DEFAULT_PAGE_SIZE)));
            parameters.add(new BasicNameValuePair("startRecord", Integer.toString(startNumber)));
            String queryValue = encodeQuery(queryString, type);
            parameters.add(new BasicNameValuePair("query", queryValue));
            HttpGet get = new HttpGet(SRU_URL);
            URI uri = new URIBuilder(get.getUri())
                    .addParameters(parameters)
                    .build();
            get.setUri(uri);
            CloseableHttpResponse response = client.execute(get);
            return processResponse(response);
        } catch (Exception e) {
            throw new AuthorityException("Cannot perform gnd query.", e);
        }
    }

    private List<GndRecord> processResponse(CloseableHttpResponse response) throws Exception {
        return xPathService.evaluateForIdentifiers(response.getEntity().getContent());
    }

    public Optional<GndRecord> getGndRecordById(String gndId) throws AuthorityException {
        try (final CloseableHttpClient client = HttpClients.createDefault()) {
            List<NameValuePair> parameters = new ArrayList<>(DEFAULT_PARAMS);
            parameters.add(new BasicNameValuePair("query", "idn=" + gndId));
            HttpGet get = new HttpGet(SRU_URL);
            URI uri = new URIBuilder(get.getUri())
                    .addParameters(parameters)
                    .build();
            get.setUri(uri);
            CloseableHttpResponse response = client.execute(get);
            List<GndRecord> records = processResponse(response);
            if (records.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(records.get(0));
        } catch (Exception e) {
            throw new AuthorityException("Cannot perform search.", e);
        }
    }

    public GeoLocation getLocationById(ControlledVocabulary voc) throws AuthorityException {
        if (voc.authority != Authority.GND) {
            throw new AuthorityException("Cannot find location without GND id.");
        }
        String gndId = voc.controlledId;
        return getGndRecordById(gndId).map(x -> x.geoLocation).orElse(null);
    }

    private GeoLocation processGeoLocationResponse(CloseableHttpResponse response) throws Exception {
        return xPathService.evaluateForGeoLocations(response.getEntity().getContent());
    }
}
