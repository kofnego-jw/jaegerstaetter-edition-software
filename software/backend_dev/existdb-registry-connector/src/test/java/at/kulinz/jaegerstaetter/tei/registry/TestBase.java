package at.kulinz.jaegerstaetter.tei.registry;

import at.kulinz.jaegerstaetter.testhelper.TestHelperConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

@SpringBootTest(classes = {RegistryConnectorConfig.class, TestHelperConfig.class})
@Profile("metadata")
public abstract class TestBase {

}
