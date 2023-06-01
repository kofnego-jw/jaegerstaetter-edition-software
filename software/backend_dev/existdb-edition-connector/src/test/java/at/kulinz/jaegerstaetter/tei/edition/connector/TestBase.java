package at.kulinz.jaegerstaetter.tei.edition.connector;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {EditionConnectorConfig.class})
@Profile("preview")
public abstract class TestBase {

}
