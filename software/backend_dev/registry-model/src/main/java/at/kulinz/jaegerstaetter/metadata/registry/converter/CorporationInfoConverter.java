package at.kulinz.jaegerstaetter.metadata.registry.converter;

import at.kulinz.jaegerstaetter.metadata.authority.gnd.GndQueryService;
import at.kulinz.jaegerstaetter.metadata.authority.gnd.GndRecord;
import at.kulinz.jaegerstaetter.metadata.authority.model.ControlledVocabulary;
import at.kulinz.jaegerstaetter.metadata.registry.model.CorporationInfo;
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
public class CorporationInfoConverter {

    public static final List<String> COLUMN_NAMES = List.of("Organisation", "key", "gnd", "Anmerkung");

    public static final CSVFormat DEFAULT_FORMAT = CSVFormat.Builder.create(CSVFormat.RFC4180)
            .setDelimiter(',').setIgnoreEmptyLines(true).build();
    private final GndQueryService gndQueryService;

    @Autowired
    public CorporationInfoConverter(GndQueryService gndQueryService) {
        this.gndQueryService = gndQueryService;
    }

    private static int position(String head) {
        return COLUMN_NAMES.indexOf(head);
    }

    public List<CorporationInfo> convert(File csv) throws Exception {
        String content = FileUtils.readFileToString(csv, StandardCharsets.UTF_8);
        CSVParser records = DEFAULT_FORMAT.parse(new StringReader(content));
        List<CorporationInfo> infos = new ArrayList<>();
        for (CSVRecord record : records) {
            String key = record.get(position(COLUMN_NAMES.get(1)));
            if (key.equals("key")) {
                continue;
            }
            if (StringUtils.isBlank(key)) {
                continue;
            }
            key = key.trim();
            String name = record.get(position(COLUMN_NAMES.get(0)));
            String gnd = record.get(position(COLUMN_NAMES.get(2)));
            String preferredName = name;
            List<ControlledVocabulary> controlledVocs = new ArrayList<>();
            if (!StringUtils.isBlank(gnd)) {
                Optional<GndRecord> opt = gndQueryService.getGndRecordById(gnd);
                if (opt.isPresent()) {
                    GndRecord gndRecord = opt.get();
                    controlledVocs.add(gndRecord.toControlledVocabulary());
                    preferredName = StringUtils.isBlank(gndRecord.preferredName) ? name : gndRecord.preferredName;
                }
            }
            String todo = "";
            String note = record.get(position(COLUMN_NAMES.get(3)));
            CorporationInfo info = new CorporationInfo(name, key, preferredName, note, todo, controlledVocs, null);
            infos.add(info);
        }
        return infos;
    }
}
