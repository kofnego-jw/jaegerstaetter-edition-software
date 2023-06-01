package at.kulinz.jaegerstaetter.metadata;

import at.kulinz.jaegerstaetter.metadata.registry.RegistryModelConfig;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest(classes = {RegistryModelConfig.class})
public abstract class TestBase {

    public static final File JSON_REPOSITORY_FILE = new File("target/test/jsonRepository.json");

    @BeforeAll
    public static void createParentDir() {
        File testDir = JSON_REPOSITORY_FILE.getParentFile();
        if (!testDir.exists() && !testDir.mkdirs()) {
            System.out.println("Cannot create test directory.");
        }
        Assumptions.assumeTrue(testDir.exists());
    }
}
