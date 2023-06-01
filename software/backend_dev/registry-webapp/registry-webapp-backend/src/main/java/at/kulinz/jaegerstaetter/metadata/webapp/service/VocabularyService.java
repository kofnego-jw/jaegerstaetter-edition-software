package at.kulinz.jaegerstaetter.metadata.webapp.service;

import at.kulinz.jaegerstaetter.metadata.authority.geonames.GeonamesQueryService;
import at.kulinz.jaegerstaetter.metadata.authority.geonames.GeonamesResult;
import at.kulinz.jaegerstaetter.metadata.authority.gnd.GndQueryService;
import at.kulinz.jaegerstaetter.metadata.authority.gnd.GndRecord;
import at.kulinz.jaegerstaetter.metadata.authority.model.ControlledVocabulary;
import at.kulinz.jaegerstaetter.metadata.authority.model.GeoLocation;
import at.kulinz.jaegerstaetter.metadata.authority.model.VocabularyType;
import at.kulinz.jaegerstaetter.metadata.authority.wikidata.WikidataQueryService;
import at.kulinz.jaegerstaetter.metadata.registry.model.PlaceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VocabularyService {

    private final GeonamesQueryService geonamesQueryService;
    private final GndQueryService gndQueryService;
    private final WikidataQueryService wikidataQueryService;

    @Autowired
    public VocabularyService(GeonamesQueryService geonamesQueryService, GndQueryService gndQueryService, WikidataQueryService wikidataQueryService) {
        this.geonamesQueryService = geonamesQueryService;
        this.gndQueryService = gndQueryService;
        this.wikidataQueryService = wikidataQueryService;
    }

    public List<GeonamesResult> searchInGeonames(String queryString, int pageNumber) throws Exception {
        return this.geonamesQueryService.search(queryString, "de", pageNumber);
    }

    public List<GndRecord> searchInGnd(String queryString, VocabularyType type, int pageNumber) throws Exception {
        return this.gndQueryService.query(queryString, pageNumber, type);
    }

    public List<ControlledVocabulary> searchInWikidata(String queryString, int pageNumber) throws Exception {
        return this.wikidataQueryService.query(queryString, "de", pageNumber);
    }

    public GeoLocation findGeoLocation(PlaceInfo pi) throws Exception {
        if (pi == null) {
            return null;
        }
        for (ControlledVocabulary voc : pi.controlledVocabularies) {
            GeoLocation location = switch (voc.authority) {
                case GND -> gndQueryService.getLocationById(voc);
                case GEONAMES -> geonamesQueryService.getLocationById(voc);
                default -> null;
            };
            if (location != null) {
                return location;
            }
        }
        return null;
    }

    public String findPreferredName(PlaceInfo pi) throws Exception {
        if (pi == null) {
            return null;
        }
        for (ControlledVocabulary voc : pi.controlledVocabularies) {
            String preferredName = switch (voc.authority) {
                case GND -> gndQueryService.getGndRecordById(voc.controlledId).map(x -> x.preferredName).orElse(null);
                case GEONAMES ->
                        geonamesQueryService.findRecordById(voc.controlledId).controlledVocabulary.preferredTitle;
                default -> null;
            };
            if (preferredName != null) {
                return preferredName;
            }
        }
        return null;
    }


}
