package at.kulinz.jaegerstaetter.metadata;

import at.kulinz.jaegerstaetter.metadata.registry.converter.CorporationInfoConverter;
import at.kulinz.jaegerstaetter.metadata.registry.converter.PersonInfoConverter;
import at.kulinz.jaegerstaetter.metadata.registry.converter.PlaceInfoConverter;
import at.kulinz.jaegerstaetter.metadata.registry.converter.SaintInfoConverter;
import at.kulinz.jaegerstaetter.metadata.registry.model.CorporationInfo;
import at.kulinz.jaegerstaetter.metadata.registry.model.PersonInfo;
import at.kulinz.jaegerstaetter.metadata.registry.model.PlaceInfo;
import at.kulinz.jaegerstaetter.metadata.registry.model.SaintInfo;
import at.kulinz.jaegerstaetter.metadata.registry.repository.JsonFileRepository;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;

import static at.kulinz.jaegerstaetter.metadata.ConverterTest.*;

public class CsvToRepository extends TestBase {

    private static final File REPO_FILE = new File("../registry/workingDir/repository/data.json");

    @Autowired
    private PersonInfoConverter personInfoConverter;
    @Autowired
    private PlaceInfoConverter placeInfoConverter;
    @Autowired
    private CorporationInfoConverter corporationInfoConverter;
    @Autowired
    private SaintInfoConverter saintInfoConverter;

    @Test
    public void test() throws Exception {
        JsonFileRepository repository = new JsonFileRepository(REPO_FILE);
        Assumptions.assumeTrue(repository.findAllCorporationInfos().isEmpty());
        Assumptions.assumeTrue(repository.findAllPersonInfos().isEmpty());
        Assumptions.assumeTrue(repository.findAllPlaceInfos().isEmpty());
        Assumptions.assumeTrue(repository.findAllSaintInfos().isEmpty());

        List<PersonInfo> personInfoList = personInfoConverter.convert(PERSON_CSV);
        for (PersonInfo info : personInfoList) {
            if (repository.findPersonByKey(info.key).isPresent()) {
                System.out.println("This person key is given multiple times: " + info.key);
                continue;
            }
            repository.addPersonInfo(info);
        }

        List<PlaceInfo> placeInfoList = placeInfoConverter.convert(PLACE_CSV);
        for (PlaceInfo info : placeInfoList) {
            if (repository.findPlaceByKey(info.key).isPresent()) {
                System.out.println("This place key is given multiple times: " + info.key);
                continue;
            }
            repository.addPlaceInfo(info);
        }

        List<CorporationInfo> corporationInfoList = corporationInfoConverter.convert(CORP_CSV);
        for (CorporationInfo info : corporationInfoList) {
            if (repository.findCorporationByKey(info.key).isPresent()) {
                System.out.println("This corporation key is given multiple times: " + info.key);
                continue;
            }
            repository.addCorporationInfo(info);
        }

        List<SaintInfo> saintInfoList = saintInfoConverter.convert(SAINT_CSV);
        for (SaintInfo info : saintInfoList) {
            if (repository.findSaintByKey(info.key).isPresent()) {
                System.out.println("This saint key is given multiple times: " + info.key);
                continue;
            }
            repository.addSaintInfo(info);
        }

    }


}
