package at.kulinz.jaegerstaetter.metadata.registry.repository;

import at.kulinz.jaegerstaetter.metadata.registry.MetadataException;
import at.kulinz.jaegerstaetter.metadata.registry.model.CorporationInfo;
import at.kulinz.jaegerstaetter.metadata.registry.model.PersonInfo;
import at.kulinz.jaegerstaetter.metadata.registry.model.PlaceInfo;
import at.kulinz.jaegerstaetter.metadata.registry.model.SaintInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

public abstract class AbstractDataRepository implements DataRepository {

    abstract JsonRepository getJsonRepository();

    abstract void writeData() throws MetadataException;

    @Override
    public List<PersonInfo> findAllPersonInfos() {
        return this.getJsonRepository().personInfoList;
    }

    @Override
    public List<PlaceInfo> findAllPlaceInfos() {
        return this.getJsonRepository().placeInfoList;
    }

    @Override
    public List<CorporationInfo> findAllCorporationInfos() {
        return this.getJsonRepository().corporationInfoList;
    }

    @Override
    public List<SaintInfo> findAllSaintInfos() {
        return this.getJsonRepository().saintInfoList;
    }

    @Override
    public void addPersonInfo(PersonInfo pi) throws MetadataException {
        if (pi == null) {
            throw new MetadataException("Cannot add null person info.");
        }
        if (StringUtils.isBlank(pi.key)) {
            throw new MetadataException("Cannot add person info without a key.");
        }
        if (this.findPersonByKey(pi.key).isPresent()) {
            throw new MetadataException("A person info with this key already exists.");
        }
        this.getJsonRepository().personInfoList.add(pi);
        this.writeData();
    }

    @Override
    public void addPlaceInfo(PlaceInfo pi) throws MetadataException {
        if (pi == null) {
            throw new MetadataException("Cannot add null place info.");
        }
        if (StringUtils.isBlank(pi.key)) {
            throw new MetadataException("Cannot add place info without a key.");
        }
        if (this.findPlaceByKey(pi.key).isPresent()) {
            throw new MetadataException("A place info with this key already exists.");
        }
        this.getJsonRepository().placeInfoList.add(pi);
        this.writeData();
    }

    @Override
    public void addCorporationInfo(CorporationInfo pi) throws MetadataException {
        if (pi == null) {
            throw new MetadataException("Cannot add null corporation info.");
        }
        if (StringUtils.isBlank(pi.key)) {
            throw new MetadataException("Cannot add corporation info without a key.");
        }
        if (this.findCorporationByKey(pi.key).isPresent()) {
            throw new MetadataException("A corporation info with this key already exists.");
        }
        this.getJsonRepository().corporationInfoList.add(pi);
        this.writeData();
    }

    @Override
    public void addSaintInfo(SaintInfo pi) throws MetadataException {
        if (pi == null) {
            throw new MetadataException("Cannot add null saint info.");
        }
        if (StringUtils.isBlank(pi.key)) {
            throw new MetadataException("Cannot add saint info without a key.");
        }
        if (this.findSaintByKey(pi.key).isPresent()) {
            throw new MetadataException("A saint info with this key already exists.");
        }
        this.getJsonRepository().saintInfoList.add(pi);
        this.writeData();
    }

    @Override
    public void removePersonInfoByKey(String key) throws MetadataException {
        Optional<PersonInfo> toDel = findPersonByKey(key);
        if (toDel.isEmpty()) {
            return;
        }
        this.getJsonRepository().personInfoList.remove(toDel.get());
        this.writeData();
    }

    @Override
    public void removePlaceInfoByKey(String key) throws MetadataException {
        Optional<PlaceInfo> toDel = findPlaceByKey(key);
        if (toDel.isEmpty()) {
            return;
        }
        this.getJsonRepository().placeInfoList.remove(toDel.get());
        this.writeData();
    }

    @Override
    public void removeCorporationInfoByKey(String key) throws MetadataException {
        Optional<CorporationInfo> toDel = findCorporationByKey(key);
        if (toDel.isEmpty()) {
            return;
        }
        this.getJsonRepository().corporationInfoList.remove(toDel.get());
        this.writeData();
    }

    @Override
    public void removeSaintInfoByKey(String key) throws MetadataException {
        Optional<SaintInfo> toDel = findSaintByKey(key);
        if (toDel.isEmpty()) {
            return;
        }
        this.getJsonRepository().saintInfoList.remove(toDel.get());
        this.writeData();
    }

    @Override
    public void clear() throws MetadataException {
        this.getJsonRepository().placeInfoList.clear();
        this.getJsonRepository().personInfoList.clear();
        this.getJsonRepository().corporationInfoList.clear();
        this.getJsonRepository().saintInfoList.clear();
        this.writeData();
    }

}
