package at.kulinz.jaegerstaetter.search;

import at.kulinz.jaegerstaetter.frontendmodel.serviceapi.ResourceService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {TestConfiguration.class})
@ActiveProfiles("preview")
public abstract class TestBase {

    @MockBean
    protected ResourceService editionResourceService;
}
