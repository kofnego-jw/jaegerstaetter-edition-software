package at.kulinz.jaegerstaetter.metadata.webapp.service;

import at.kulinz.jaegerstaetter.metadata.authority.model.GeoLocation;
import at.kulinz.jaegerstaetter.metadata.registry.model.CorporationInfo;
import at.kulinz.jaegerstaetter.metadata.registry.model.PersonInfo;
import at.kulinz.jaegerstaetter.metadata.registry.model.PlaceInfo;
import at.kulinz.jaegerstaetter.metadata.registry.model.SaintInfo;
import at.kulinz.jaegerstaetter.metadata.registry.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RepositoryService {

    private final DataRepository dataRepository;

    private final VocabularyService vocabularyService;

    @Autowired
    public RepositoryService(DataRepository dataRepository, VocabularyService vocabularyService) {
        this.dataRepository = dataRepository;
        this.vocabularyService = vocabularyService;
    }

    public List<PersonInfo> findAllPersons() {
        return this.dataRepository.findAllPersonInfos();
    }

    public List<PlaceInfo> findAllPlaces() {
        return this.dataRepository.findAllPlaceInfos();
    }

    public List<CorporationInfo> findAllCorporations() {
        return this.dataRepository.findAllCorporationInfos();
    }

    public List<SaintInfo> findAllSaints() {
        return this.dataRepository.findAllSaintInfos();
    }

    public List<CorporationInfo> addCorporationInfo(CorporationInfo info) {
        try {
            this.dataRepository.addCorporationInfo(info);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot add corporation info.", e);
        }
        return findAllCorporations();
    }

    public List<SaintInfo> addSaintInfo(SaintInfo info) {
        try {
            this.dataRepository.addSaintInfo(info);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot add saint info.", e);
        }
        return findAllSaints();
    }

    public Optional<PlaceInfo> addPlaceInfo(PlaceInfo newPlaceInfo) {
        PlaceInfo placeInfo = new PlaceInfo(Objects.toString(newPlaceInfo.locationName, ""),
                Objects.toString(newPlaceInfo.region, ""), Objects.toString(newPlaceInfo.key, ""),
                Objects.toString(newPlaceInfo.preferredName, ""), Objects.toString(newPlaceInfo.note, ""),
                Objects.toString(newPlaceInfo.todo, ""), newPlaceInfo.controlledVocabularies, newPlaceInfo.geoLocation, newPlaceInfo.generatedName);
        try {
            this.dataRepository.addPlaceInfo(placeInfo);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot add place info.", e);
        }
        return this.dataRepository.findPlaceByKey(placeInfo.key);
    }

    public List<PersonInfo> addPersonInfo(PersonInfo pi) {
        try {
            this.dataRepository.addPersonInfo(pi);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot add person info.", e);
        }
        return findAllPersons();
    }

    public List<CorporationInfo> deleteCorporationInfo(String key) {
        try {
            this.dataRepository.removeCorporationInfoByKey(key);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot remove corporation.", e);
        }
        return findAllCorporations();
    }

    public List<SaintInfo> deleteSaintInfo(String key) {
        try {
            this.dataRepository.removeSaintInfoByKey(key);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot remove saint.", e);
        }
        return findAllSaints();

    }

    public List<PersonInfo> deletePersonInfo(String key) {
        try {
            this.dataRepository.removePersonInfoByKey(key);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot remove person.", e);
        }
        return findAllPersons();
    }

    public List<PlaceInfo> deletePlaceInfo(String key) {
        try {
            this.dataRepository.removePlaceInfoByKey(key);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot remove place.", e);
        }
        return findAllPlaces();
    }

    public List<CorporationInfo> updateCorporationInfo(CorporationInfo info) {
        try {
            this.deleteCorporationInfo(info.key);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot update: Error while delete old data.", e);
        }
        try {
            this.addCorporationInfo(info);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot update: Error while insert new data.", e);
        }
        return this.findAllCorporations();
    }

    public List<SaintInfo> updateSaintInfo(SaintInfo info) {
        try {
            this.deleteSaintInfo(info.key);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot update: Error while delete old data.", e);
        }
        try {
            this.addSaintInfo(info);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot update: Error while insert new data.", e);
        }
        return this.findAllSaints();
    }

    public List<PersonInfo> updatePersonInfo(PersonInfo pi) {
        try {
            this.deletePersonInfo(pi.key);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot update: Error while delete old data.", e);
        }
        try {
            this.addPersonInfo(pi);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot update: Error while insert new data.", e);
        }
        return this.findAllPersons();
    }

    public Optional<PlaceInfo> updatePlaceInfo(PlaceInfo pi) {
        try {
            this.deletePlaceInfo(pi.key);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot update: Error while delete old data.", e);
        }
        try {
            return this.addPlaceInfo(pi);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot update: Error while insert new data.", e);
        }
    }

    public List<PlaceInfo> autoAddInformation(PlaceInfo pi) throws Exception {
        GeoLocation location = vocabularyService.findGeoLocation(pi);
        if (location != null) {
            PlaceInfo info = new PlaceInfo(pi.locationName, pi.region, pi.key, pi.preferredName, pi.note, pi.todo, pi.controlledVocabularies, location, pi.generatedName);
            updatePlaceInfo(info);
        }
        if (pi.preferredName == null) {
            String preferredName = vocabularyService.findPreferredName(pi);
            PlaceInfo info = new PlaceInfo(pi.locationName, pi.region, pi.key, preferredName, pi.note, pi.todo, pi.controlledVocabularies, location, pi.generatedName);
            updatePlaceInfo(info);
        }
        return findAllPlaces();
    }


}
