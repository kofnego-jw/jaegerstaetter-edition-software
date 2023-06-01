package at.kulinz.jaegerstaetter.metadata.registry.repository;

import at.kulinz.jaegerstaetter.metadata.registry.MetadataException;
import at.kulinz.jaegerstaetter.metadata.registry.model.CorporationInfo;
import at.kulinz.jaegerstaetter.metadata.registry.model.PersonInfo;
import at.kulinz.jaegerstaetter.metadata.registry.model.PlaceInfo;
import at.kulinz.jaegerstaetter.metadata.registry.model.SaintInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface DataRepository {

    List<PersonInfo> findAllPersonInfos();

    default Optional<PersonInfo> findPersonByKey(String key) {
        if (StringUtils.isBlank(key)) {
            return Optional.empty();
        }
        return findAllPersonInfos().stream().filter(pi -> key.equals(pi.key))
                .findAny();
    }

    default List<PersonInfo> findPersonByName(String name) {
        return findAllPersonInfos().stream().filter(pi -> pi.getReadableName().contains(name))
                .collect(Collectors.toList());
    }

    void addPersonInfo(PersonInfo pi) throws MetadataException;

    void removePersonInfoByKey(String key) throws MetadataException;

    List<PlaceInfo> findAllPlaceInfos();

    default Optional<PlaceInfo> findPlaceByKey(String key) {
        if (StringUtils.isBlank(key)) {
            return Optional.empty();
        }
        return findAllPlaceInfos().stream().filter(pi -> key.equals(pi.key))
                .findAny();
    }

    default List<PlaceInfo> findPlaceByName(String name) {
        return findAllPlaceInfos().stream().filter(pi -> pi.getReadableName().contains(name))
                .collect(Collectors.toList());
    }

    void addPlaceInfo(PlaceInfo pi) throws MetadataException;

    void removePlaceInfoByKey(String key) throws MetadataException;

    List<CorporationInfo> findAllCorporationInfos();

    default Optional<CorporationInfo> findCorporationByKey(String key) {
        if (StringUtils.isBlank(key)) {
            return Optional.empty();
        }
        return findAllCorporationInfos().stream().filter(ci -> key.equals(ci.key))
                .findAny();
    }

    default List<CorporationInfo> findCorporationByName(String name) {
        return findAllCorporationInfos().stream().filter(pi -> pi.organisation.contains(name))
                .collect(Collectors.toList());
    }

    void addCorporationInfo(CorporationInfo pi) throws MetadataException;

    void removeCorporationInfoByKey(String key) throws MetadataException;

    List<SaintInfo> findAllSaintInfos();

    default Optional<SaintInfo> findSaintByKey(String key) {
        if (StringUtils.isBlank(key)) {
            return Optional.empty();
        }
        return findAllSaintInfos().stream().filter(pi -> key.equals(pi.key))
                .findAny();
    }

    default List<SaintInfo> findSaintByName(String name) {
        return findAllSaintInfos().stream().filter(pi -> pi.getReadableName().contains(name))
                .collect(Collectors.toList());
    }

    void addSaintInfo(SaintInfo pi) throws MetadataException;

    void removeSaintInfoByKey(String key) throws MetadataException;

    void clear() throws MetadataException;

}
