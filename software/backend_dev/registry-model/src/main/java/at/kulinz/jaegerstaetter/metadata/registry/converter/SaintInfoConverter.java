package at.kulinz.jaegerstaetter.metadata.registry.converter;

import at.kulinz.jaegerstaetter.metadata.authority.gnd.GndQueryService;
import at.kulinz.jaegerstaetter.metadata.authority.gnd.GndRecord;
import at.kulinz.jaegerstaetter.metadata.authority.model.ControlledVocabulary;
import at.kulinz.jaegerstaetter.metadata.authority.model.Sex;
import at.kulinz.jaegerstaetter.metadata.registry.model.SaintInfo;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SaintInfoConverter {

    public static final List<String> COLUMN_NAMES = List.of("forname", "key", "Bezeichnung",
            "Link Heiligenlexikon", "GND", "surname", "forname", "addName", "rolename", "birth",
            "birthplace", "residence", "death", "deathplace", "sex", "Anmerkung", "To do");

    public static final CSVFormat DEFAULT_FORMAT = CSVFormat.Builder.create(CSVFormat.RFC4180)
            .setDelimiter(',').setIgnoreEmptyLines(true).build();
    private final GndQueryService gndQueryService;

    @Autowired
    public SaintInfoConverter(GndQueryService gndQueryService) {
        this.gndQueryService = gndQueryService;
    }

    private static int position(String head) {
        return COLUMN_NAMES.lastIndexOf(head);
    }

    private static List<String> splitByComma(String value) {
        if (StringUtils.isBlank(value)) {
            return Collections.emptyList();
        }
        return Stream.of(value.split("\\s*,\\s*")).collect(Collectors.toList());
    }

    private static Sex sex(String s) {
        if (StringUtils.isBlank(s)) {
            return null;
        }
        if (s.trim().toLowerCase().startsWith("f")) {
            return Sex.FEMALE;
        }
        return Sex.MALE;
    }


    public List<SaintInfo> convert(File csv) throws Exception {
        String content = FileUtils.readFileToString(csv, StandardCharsets.UTF_8);
        CSVParser records = DEFAULT_FORMAT.parse(new StringReader(content));
        List<SaintInfo> infos = new ArrayList<>();
        for (CSVRecord record : records) {
            String key = record.get(position(COLUMN_NAMES.get(1)));
            if (key.equals("key")) {
                continue;
            }
            if (StringUtils.isBlank(key)) {
                continue;
            }
            key = key.trim();
            String title = record.get(position(COLUMN_NAMES.get(2)));
            String lexikonLink = record.get(position(COLUMN_NAMES.get(3)));
            String surname = record.get(position("surname"));
            String forename = record.get(6);
            List<String> addNames = Collections.emptyList();
            List<String> roleNames = splitByComma(record.get(position("rolename")));
            String birthDate = record.get(position("birth"));
            String birthPlace = record.get(position("birthplace"));
            String preferredName = title;
            List<String> residences = splitByComma(record.get(position("residence")));
            String deathDate = record.get(position("death"));
            String deathPlace = record.get(position("deathplace"));
            Sex sex = sex(record.get(position("sex")));
            String note = record.get(position("Anmerkung"));
            String gnd = record.get(position("GND"));
            String todo = record.get(position("To do"));
            List<ControlledVocabulary> controlledVocs = new ArrayList<>();
            if (!StringUtils.isBlank(gnd)) {
                gnd = gnd.trim();
                Optional<GndRecord> opt = gndQueryService.getGndRecordById(gnd);
                if (opt.isPresent()) {
                    GndRecord gndRecord = opt.get();
                    preferredName = gndRecord.preferredName;
                    controlledVocs.add(gndRecord.toControlledVocabulary());
                }
            }
            SaintInfo info = new SaintInfo(key, title, preferredName, lexikonLink, controlledVocs, surname, forename, addNames,
                    roleNames, birthDate, birthPlace, residences, deathDate, deathPlace, sex, note, todo, null);
            infos.add(info);
        }
        return infos;
    }
}
