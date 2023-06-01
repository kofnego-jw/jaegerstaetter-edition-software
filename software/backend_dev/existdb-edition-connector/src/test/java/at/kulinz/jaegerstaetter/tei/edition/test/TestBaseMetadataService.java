package at.kulinz.jaegerstaetter.tei.edition.test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@Profile("preview")
@ContextConfiguration(classes = {MetadataTestConfiguration.class})
public abstract class TestBaseMetadataService {

}
