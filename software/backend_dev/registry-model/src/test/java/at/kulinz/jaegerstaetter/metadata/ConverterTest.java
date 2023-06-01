package at.kulinz.jaegerstaetter.metadata;

import at.kulinz.jaegerstaetter.metadata.registry.converter.CorporationInfoConverter;
import at.kulinz.jaegerstaetter.metadata.registry.converter.PersonInfoConverter;
import at.kulinz.jaegerstaetter.metadata.registry.converter.PlaceInfoConverter;
import at.kulinz.jaegerstaetter.metadata.registry.converter.SaintInfoConverter;
import at.kulinz.jaegerstaetter.metadata.registry.model.CorporationInfo;
import at.kulinz.jaegerstaetter.metadata.registry.model.PersonInfo;
import at.kulinz.jaegerstaetter.metadata.registry.model.PlaceInfo;
import at.kulinz.jaegerstaetter.metadata.registry.model.SaintInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;

public class ConverterTest extends TestBase {

    public static final File PERSON_CSV = new File("../../issue-007/src/test/resources/keygenerator/Personenverzeichnis - PersonenVZ.csv");
    public static final File PLACE_CSV = new File("../../issue-007/src/test/resources/keygenerator/Personenverzeichnis - OrtsVZ.csv");
    public static final File SAINT_CSV = new File("../../issue-007/src/test/resources/keygenerator/Personenverzeichnis - HeiligenVZ.csv");
    public static final File CORP_CSV = new File("../../issue-007/src/test/resources/keygenerator/Personenverzeichnis - OrganisationsVZ.csv");

    @Autowired
    private CorporationInfoConverter corporationInfoConverter;

    @Autowired
    private PersonInfoConverter personInfoConverter;

    @Autowired
    private PlaceInfoConverter placeInfoConverter;

    @Autowired
    private SaintInfoConverter saintInfoConverter;

    @Test
    public void test_convertPerson() throws Exception {
        List<PersonInfo> infos = personInfoConverter.convert(PERSON_CSV);
        infos.forEach(System.out::println);
        System.out.println(infos.size());
    }

    @Test
    public void test_convertPlace() throws Exception {
        List<PlaceInfo> infos = placeInfoConverter.convert(PLACE_CSV);
        infos.forEach(System.out::println);
        System.out.println(infos.size());
    }

    @Test
    public void test_convertCorp() throws Exception {
        List<CorporationInfo> infos = corporationInfoConverter.convert(CORP_CSV);
        infos.forEach(System.out::println);
        System.out.println(infos.size());
    }

    @Test
    public void test_convertSaint() throws Exception {
        List<SaintInfo> infos = saintInfoConverter.convert(SAINT_CSV);
        infos.forEach(System.out::println);
        System.out.println(infos.size());
    }
}
