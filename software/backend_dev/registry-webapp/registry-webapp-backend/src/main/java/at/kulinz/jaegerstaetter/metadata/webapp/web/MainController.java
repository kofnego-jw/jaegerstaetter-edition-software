package at.kulinz.jaegerstaetter.metadata.webapp.web;

import at.kulinz.jaegerstaetter.metadata.authority.geonames.GeonamesResult;
import at.kulinz.jaegerstaetter.metadata.authority.gnd.GndRecord;
import at.kulinz.jaegerstaetter.metadata.authority.model.ControlledVocabulary;
import at.kulinz.jaegerstaetter.metadata.authority.model.VocabularyType;
import at.kulinz.jaegerstaetter.metadata.registry.model.CorporationInfo;
import at.kulinz.jaegerstaetter.metadata.registry.model.PersonInfo;
import at.kulinz.jaegerstaetter.metadata.registry.model.PlaceInfo;
import at.kulinz.jaegerstaetter.metadata.registry.model.SaintInfo;
import at.kulinz.jaegerstaetter.metadata.webapp.service.RepositoryService;
import at.kulinz.jaegerstaetter.metadata.webapp.service.VocabularyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private VocabularyService vocabularyService;

    @GetMapping("/person/")
    public List<PersonInfo> findAllPersons() {
        return this.repositoryService.findAllPersons();
    }

    @PutMapping("/person/")
    public List<PersonInfo> addNewPerson(@RequestBody PersonInfo personInfo) {
        return this.repositoryService.addPersonInfo(personInfo);
    }

    @PostMapping("/person/")
    public List<PersonInfo> updatePerson(@RequestBody PersonInfo personInfo) {
        return this.repositoryService.updatePersonInfo(personInfo);
    }

    @DeleteMapping("/person/{key}")
    public List<PersonInfo> deletePerson(@PathVariable("key") String key) {
        return this.repositoryService.deletePersonInfo(key);
    }

    @GetMapping("/place/")
    public List<PlaceInfo> findAllPlaces() {
        return this.repositoryService.findAllPlaces();
    }

    @PutMapping("/place/")
    public List<PlaceInfo> addNewPlace(@RequestBody PlaceInfo placeInfo) throws Exception {
        Optional<PlaceInfo> optional = this.repositoryService.addPlaceInfo(placeInfo);
        if (optional.isPresent()) {
            PlaceInfo pi = optional.get();
            if (pi.geoLocation == null && !pi.controlledVocabularies.isEmpty()) {
                return autoAddInformation(pi);
            }
        }
        return this.repositoryService.findAllPlaces();
    }

    @PostMapping("/place/")
    public List<PlaceInfo> updatePlace(@RequestBody PlaceInfo placeInfo) throws Exception {
        Optional<PlaceInfo> optional = this.repositoryService.updatePlaceInfo(placeInfo);
        if (optional.isPresent()) {
            PlaceInfo pi = optional.get();
            if (pi.geoLocation == null && !pi.controlledVocabularies.isEmpty()) {
                return autoAddInformation(pi);
            }
        }
        return this.repositoryService.findAllPlaces();
    }

    @PostMapping("/geoLocation")
    public List<PlaceInfo> autoAddInformation(@RequestBody PlaceInfo pi) throws Exception {
        return repositoryService.autoAddInformation(pi);
    }

    @DeleteMapping("/place/{key}")
    public List<PlaceInfo> deletePlace(@PathVariable("key") String key) {
        return this.repositoryService.deletePlaceInfo(key);
    }

    @GetMapping("/corporation/")
    public List<CorporationInfo> findAllCorporations() {
        return this.repositoryService.findAllCorporations();
    }

    @PutMapping("/corporation/")
    public List<CorporationInfo> addNewCorporation(@RequestBody CorporationInfo corporationInfo) {
        return this.repositoryService.addCorporationInfo(corporationInfo);
    }

    @PostMapping("/corporation/")
    public List<CorporationInfo> updateCorporation(@RequestBody CorporationInfo corporationInfo) {
        return this.repositoryService.updateCorporationInfo(corporationInfo);
    }

    @DeleteMapping("/corporation/{key}")
    public List<CorporationInfo> deleteCorporation(@PathVariable("key") String key) {
        return this.repositoryService.deleteCorporationInfo(key);
    }

    @GetMapping("/saint/")
    public List<SaintInfo> findAllSaints() {
        return this.repositoryService.findAllSaints();
    }

    @PutMapping("/saint/")
    public List<SaintInfo> addNewSaint(@RequestBody SaintInfo saintInfo) {
        return this.repositoryService.addSaintInfo(saintInfo);
    }

    @PostMapping("/saint/")
    public List<SaintInfo> updateSaint(@RequestBody SaintInfo saintInfo) {
        return this.repositoryService.updateSaintInfo(saintInfo);
    }

    @DeleteMapping("/saint/{key}")
    public List<SaintInfo> deleteSaint(@PathVariable("key") String key) {
        return this.repositoryService.deleteSaintInfo(key);
    }

    @PostMapping("/search/gnd/{type}/{pageNumber}")
    public List<GndRecord> searchGnd(@PathVariable("type") String type, @RequestParam("queryString") String queryString,
                                     @PathVariable("pageNumber") int pageNumber) throws Exception {
        VocabularyType vocType = VocabularyType.valueOf(type.toUpperCase());
        return vocabularyService.searchInGnd(queryString, vocType, pageNumber);
    }

    @PostMapping("/search/geonames/{pageNumber}")
    public List<GeonamesResult> searchGeonames(@RequestParam("queryString") String queryString,
                                               @PathVariable("pageNumber") int pageNumber) throws Exception {
        return vocabularyService.searchInGeonames(queryString, pageNumber);
    }

    @PostMapping("/search/wikidata/{type}/{pageNumber}")
    public List<ControlledVocabulary> searchWikidata(@RequestParam("queryString") String queryString,
                                                     @PathVariable("pageNumber") int pageNumber) throws Exception {
        return vocabularyService.searchInWikidata(queryString, pageNumber);
    }


}
