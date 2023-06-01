package at.kulinz.jaegerstaetter.integration;

import at.kulinz.jaegerstaetter.stylesheets.StylesheetsConfig;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;

@SpringBootTest
@ContextConfiguration(classes = {StylesheetsConfig.class})
public abstract class TestBase {
    @BeforeAll
    public static void copyRegistryData() throws Exception {
        File from = new File("../../../workingDir/repository/data.json");
        File to = new File("workingDir/repository/data.json");
        FileUtils.copyFile(from, to);
    }
}
