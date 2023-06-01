package at.kulinz.jaegerstaetter.correpplaces;

import at.kulinz.jaegerstaetter.correspplaces.CorrespPlacesConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {CorrespPlacesConfig.class})
public abstract class TestBase {
}
