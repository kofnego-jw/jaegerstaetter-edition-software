package at.kulinz.jaegerstaetter.metadata;

import at.kulinz.jaegerstaetter.metadata.registry.model.PersonInfo;
import at.kulinz.jaegerstaetter.metadata.registry.repository.JsonFileRepository;
import at.kulinz.jaegerstaetter.metadata.registry.repository.NameTriple;
import at.kulinz.jaegerstaetter.metadata.registry.repository.PersonNameHelper;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PersonNameHelperTest {

    @Test
    public void testNameParser() throws Exception {
        Assertions.assertEquals(NameTriple.EMPTY, PersonNameHelper.convertToNameTriple(""));
        Assertions.assertEquals(new NameTriple("Müller", "Sandro", Collections.emptyList()),
                PersonNameHelper.convertToNameTriple("Müller, Sandro"));
        Assertions.assertEquals(new NameTriple("Müller", "Sandro", Collections.emptyList()),
                PersonNameHelper.convertToNameTriple("Müller, Sandro "));
        Assertions.assertEquals(new NameTriple("Müller", "Sandro", Collections.emptyList()),
                PersonNameHelper.convertToNameTriple(" Müller, Sandro "));
        Assertions.assertEquals(new NameTriple("Müller", "Sandro", List.of("Schwein")),
                PersonNameHelper.convertToNameTriple(" Müller, Sandro (Schwein)"));
        Assertions.assertEquals(new NameTriple("Sandro", "", List.of("Schwein")),
                PersonNameHelper.convertToNameTriple("Sandro (Schwein)"));
        Assertions.assertEquals(new NameTriple("Müller", "Sandro", List.of("Schwein")),
                PersonNameHelper.convertToNameTriple(" Müller, Sandro (Schwein)"));

        Assertions.assertEquals(new NameTriple("Lenzauer, geb. Huber", "Maria Magdalena", List.of("Marie", "Prinzessin")),
                PersonNameHelper.convertToNameTriple("Lenzauer, geb. Huber, Maria Magdalena (Marie, Prinzessin)"));
        Assertions.assertEquals(new NameTriple("geb. Huber", "Maria Magdalena", List.of("Marie", "Prinzessin")),
                PersonNameHelper.convertToNameTriple("geb. Huber, Maria Magdalena (Marie, Prinzessin)"));
    }

    @Test
    public void testGenerateNames() throws Exception {
        File repoFileOrig = new File("src/test/resources/data.json");
        File repoFileCopied = new File("target/test/data.json");
        repoFileCopied.getParentFile().mkdirs();
        FileUtils.copyFile(repoFileOrig, repoFileCopied);
        JsonFileRepository repository = new JsonFileRepository(repoFileCopied);
        List<PersonInfo> allPersonInfos = repository.findAllPersonInfos();
        allPersonInfos.stream().sorted(Comparator.comparing(PersonInfo::getGeneratedOfficialName))
                .forEach(pi -> {
                    // System.out.println(pi.getPreferredName());
                    Assertions.assertNotNull(pi.getPreferredName());
                });
        repository.writeData();
    }

    @Test
    public void testJsonRepo() throws Exception {
        File input = new File("src/test/resources/data.json");
        JsonFileRepository repo = new JsonFileRepository(input);
        repo.getJsonRepository().personInfoList.forEach(pi -> {
            System.out.println(pi.getGeneratedReadableName() + " / " + pi.getGeneratedOfficialName());
        });
    }

}
