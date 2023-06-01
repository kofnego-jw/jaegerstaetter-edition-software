package at.kulinz.jaegerstaetter.metadata.registry.repository;

import at.kulinz.jaegerstaetter.metadata.registry.MetadataException;
import at.kulinz.jaegerstaetter.metadata.registry.model.*;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface TeiExporter {

    Comparator<AbstractInfo> SORT_BY_PREFERRED_NAME = (info1, info2) -> {
        if (info1 == null) {
            return info2 == null ? 0 : 1;
        }
        if (info2 == null) {
            return -1;
        }
        String pref1 = info1.getPreferredName();
        String pref2 = info2.getPreferredName();
        if (pref1 == null) {
            return pref2 == null ? 0 : 1;
        }
        if (pref2 == null) {
            return -1;
        }
        if (!pref1.equals(pref2)) {
            return pref1.compareToIgnoreCase(pref2);
        }
        String key1 = info1.getKey();
        String key2 = info2.getKey();
        if (key1 == null) {
            return key2 == null ? 0 : 1;
        }
        if (key2 == null) {
            return -1;
        }
        if (!key1.equals(key2)) {
            return key1.compareToIgnoreCase(key2);
        }
        return 0;
    };

    byte[] exportToJson(List<PersonInfo> personInfos, List<PlaceInfo> placeInfos, List<CorporationInfo> corporationInfos, List<SaintInfo> saintInfos) throws MetadataException;

    default byte[] exportToJson(DataRepository dataRepository) throws MetadataException {
        List<PersonInfo> personInfos = dataRepository.findAllPersonInfos();
        List<PlaceInfo> placeInfos = dataRepository.findAllPlaceInfos();
        List<CorporationInfo> corporateInfos = dataRepository.findAllCorporationInfos();
        List<SaintInfo> saintInfos = dataRepository.findAllSaintInfos();
        return exportToJson(personInfos, placeInfos, corporateInfos, saintInfos);
    }

    byte[] exportToTei(List<PersonInfo> personInfos, List<PlaceInfo> placeInfos, List<CorporationInfo> corporationInfos, List<SaintInfo> saintInfos) throws MetadataException;

    default byte[] exportToTei(DataRepository dataRepository) throws MetadataException {
        List<PersonInfo> personInfos = dataRepository.findAllPersonInfos()
                .stream()
                .filter(Objects::nonNull)
                .sorted(SORT_BY_PREFERRED_NAME)
                .collect(Collectors.toList());
        List<PlaceInfo> placeInfos = dataRepository.findAllPlaceInfos()
                .stream().sorted(SORT_BY_PREFERRED_NAME)
                .collect(Collectors.toList());
        List<CorporationInfo> corporateInfos = dataRepository.findAllCorporationInfos()
                .stream().sorted(SORT_BY_PREFERRED_NAME)
                .collect(Collectors.toList());
        List<SaintInfo> saintInfos = dataRepository.findAllSaintInfos()
                .stream().sorted(SORT_BY_PREFERRED_NAME)
                .collect(Collectors.toList());
        return exportToTei(personInfos, placeInfos, corporateInfos, saintInfos);
    }
}
