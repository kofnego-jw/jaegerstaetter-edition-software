package at.kulinz.jaegerstaetter.metadata.registry.converter;

import at.kulinz.jaegerstaetter.metadata.authority.geonames.GeonamesQueryService;
import at.kulinz.jaegerstaetter.metadata.authority.geonames.GeonamesResult;
import at.kulinz.jaegerstaetter.metadata.authority.gnd.GndQueryService;
import at.kulinz.jaegerstaetter.metadata.authority.gnd.GndRecord;
import at.kulinz.jaegerstaetter.metadata.authority.model.ControlledVocabulary;
import at.kulinz.jaegerstaetter.metadata.authority.model.GeoLocation;
import at.kulinz.jaegerstaetter.metadata.registry.model.PlaceInfo;
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
import java.util.List;
import java.util.Optional;

@Component
public class PlaceInfoConverter {

    public static final List<String> COLUMN_NAMES = List.of(
            "Ort",
            "Region (Bundesland bei Ö u. D, heutige Ländernamen bei anderen Regionen",
            "key",
            "Anmerkungen", "geonames", "gnd", "to do");

    public static final CSVFormat DEFAULT_FORMAT = CSVFormat.Builder.create(CSVFormat.RFC4180)
            .setDelimiter(',').setIgnoreEmptyLines(true).build();
    private final GndQueryService gndQueryService;
    private final GeonamesQueryService geonamesQueryService;

    @Autowired
    public PlaceInfoConverter(GndQueryService gndQueryService, GeonamesQueryService geonamesQueryService) {
        this.gndQueryService = gndQueryService;
        this.geonamesQueryService = geonamesQueryService;
    }

    private static int position(String head) {
        return COLUMN_NAMES.indexOf(head);
    }

    public List<PlaceInfo> convert(File csv) throws Exception {
        String content = FileUtils.readFileToString(csv, StandardCharsets.UTF_8);
        CSVParser records = DEFAULT_FORMAT.parse(new StringReader(content));
        List<PlaceInfo> infos = new ArrayList<>();
        for (CSVRecord record : records) {
            String key = record.get(position(COLUMN_NAMES.get(2)));
            if (key.equals("key")) {
                continue;
            }
            if (StringUtils.isBlank(key)) {
                continue;
            }
            key = key.trim();
            String name = record.get(position(COLUMN_NAMES.get(0)));
            String region = record.get(position(COLUMN_NAMES.get(1)));
            String note = record.get(position(COLUMN_NAMES.get(3)));
            String geonames = record.get(position(COLUMN_NAMES.get(4)));
            String gnd = record.get(position(COLUMN_NAMES.get(5)));
            String todo = record.get(position("to do"));
            String preferredName = null;
            List<ControlledVocabulary> controlledVocs = new ArrayList<>();
            GeoLocation geoLocation = null;
            if (!StringUtils.isBlank(gnd)) {
                Optional<GndRecord> opt = gndQueryService.getGndRecordById(gnd.trim());
                if (opt.isPresent()) {
                    GndRecord gndRecord = opt.get();
                    preferredName = gndRecord.preferredName;
                    geoLocation = gndRecord.geoLocation;
                    controlledVocs.add(gndRecord.toControlledVocabulary());
                }
            }
            if (!StringUtils.isBlank(geonames)) {
                GeonamesResult geonamesResult = geonamesQueryService.findRecordById(geonames);
                if (preferredName == null) {
                    preferredName = geonamesResult.toControlledVocabulary().preferredTitle;
                }
                if (geoLocation == null) {
                    geoLocation = geonamesResult.geoLocation;
                }
                controlledVocs.add(geonamesResult.toControlledVocabulary());
            }
            PlaceInfo info = new PlaceInfo(name, region, key, preferredName, note, todo, controlledVocs, geoLocation, null);
            infos.add(info);
        }
        return infos;
    }
}
