package at.kulinz.jaegerstaetter.metadata.registry.repository;

import at.kulinz.jaegerstaetter.metadata.registry.model.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.text.Collator;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JsonRepository {

    private static final Pattern REF_PATTERN = Pattern.compile("^P_\\d+");

    private static final Collator GERMAN_COLLATOR = Collator.getInstance(Locale.GERMAN);

    public static final Comparator<PersonInfo> SORT_BY_KEY = (p1, p2) -> {
        String key1 = p1.getGeneratedOfficialName();
        String key2 = p2.getGeneratedOfficialName();
        return GERMAN_COLLATOR.compare(key1, key2);
    };

    public static final Comparator<AbstractInfo> INFO_COMPARATOR = (i1, i2) -> {
        if (i1 == null) {
            return i2 == null ? 0 : 1;
        }
        if (i2 == null) {
            return -1;
        }
        return GERMAN_COLLATOR.compare(i1.getPreferredName(), i2.getPreferredName());
    };


    public final List<PersonInfo> personInfoList;
    public final List<PlaceInfo> placeInfoList;
    public final List<CorporationInfo> corporationInfoList;
    public final List<SaintInfo> saintInfoList;

    @JsonCreator
    public JsonRepository(@JsonProperty("personInfoList") List<PersonInfo> personInfoList,
                          @JsonProperty("placeInfoList") List<PlaceInfo> placeInfoList,
                          @JsonProperty("corporationInfoList") List<CorporationInfo> corporationInfoList,
                          @JsonProperty("saintInfoList") List<SaintInfo> saintInfoList) {
        this.personInfoList = new ArrayList<>();
        if (personInfoList != null) {
            this.personInfoList.addAll(personInfoList);
        }
        this.placeInfoList = new ArrayList<>();
        if (placeInfoList != null) {
            this.placeInfoList.addAll(placeInfoList);
        }
        this.corporationInfoList = new ArrayList<>();
        if (corporationInfoList != null) {
            this.corporationInfoList.addAll(corporationInfoList.stream().filter(Objects::nonNull).collect(Collectors.toList()));
        }
        this.saintInfoList = new ArrayList<>();
        if (saintInfoList != null) {
            this.saintInfoList.addAll(saintInfoList);
        }
        this.generateNames();
    }

    private void generateNames() {
        this.personInfoList.forEach(PersonInfo::clearGeneratedNames);
        this.personInfoList.forEach(pi -> {
            if (!StringUtils.isBlank(pi.preferredName)) {
                if (pi.preferredName.contains(",")) {
                    NameTriple parsed = PersonNameHelper.convertToNameTriple(pi.preferredName);
                    if (!parsed.equals(NameTriple.EMPTY)) {
                        pi.setGeneratedReadableName(parsed.generateReadableName());
                        pi.setGeneratedOfficialName(parsed.generateOfficialName());
                    }
                } else {
                    pi.setGeneratedOfficialName(pi.preferredName);
                    pi.setGeneratedReadableName(pi.preferredName);
                }
            }
        });
        this.personInfoList.stream().filter(x -> x.generatedReadableName == null).forEach(pi -> {
            if (hasUniqueSurAndForename(pi)) {
                NameTriple created = new NameTriple(pi.surname, pi.forename, Collections.emptyList());
                pi.setGeneratedOfficialName(created.generateOfficialName());
                pi.setGeneratedReadableName(created.generateReadableName());
            } else {
                NameTriple created = new NameTriple(pi.surname, pi.forename, pi.addNames);
                pi.setGeneratedOfficialName(created.generateOfficialName());
                pi.setGeneratedReadableName(created.generateReadableName());
            }
        });
        this.personInfoList.stream().filter(x -> StringUtils.isAnyBlank(x.generatedReadableName, x.generatedOfficialName)).forEach(pi -> {
            String generated = "[" + pi.key + "]";
            pi.setGeneratedReadableName(generated);
            pi.setGeneratedOfficialName(generated);
        });
        this.corporationInfoList.forEach(CorporationInfo::clearGeneratedName);
        this.corporationInfoList.forEach(ci -> ci.generatedName = ci.getReadableName());
        this.placeInfoList.forEach(PlaceInfo::clearGeneratedName);
        this.placeInfoList.forEach(pi -> {
            if (hasUniqueName(pi)) {
                pi.generatedName = pi.getReadableName();
            } else {
                String name = pi.getReadableName();
                if (!StringUtils.isBlank(pi.region)) {
                    name = name + " (" + pi.region + ")";
                }
                pi.generatedName = name;
            }
        });
        this.saintInfoList.forEach(SaintInfo::clearGeneratedName);
        this.saintInfoList.forEach(si -> si.generatedName = si.getReadableName());
    }

    private boolean hasUniqueSurAndForename(PersonInfo pi) {
        return this.personInfoList.stream().noneMatch(info -> info.surname.equals(pi.surname) && info.forename.equals(pi.forename) && !info.key.equals(pi.key));
    }

    private boolean hasUniqueName(PlaceInfo pi) {
        return this.placeInfoList.stream().noneMatch(info -> info.locationName.equals(pi.locationName) && !info.key.equals(pi.key));
    }

    public JsonRepository sorted() {
        personInfoList.sort(SORT_BY_KEY);
        placeInfoList.sort(INFO_COMPARATOR);
        corporationInfoList.sort(INFO_COMPARATOR);
        saintInfoList.sort(INFO_COMPARATOR);
        this.generateNames();
        return this;
    }
}
