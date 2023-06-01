package at.kulinz.jaegerstaetter.edition.admin;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.testhelper.service.ExistConnectionTestHelper;
import org.junit.jupiter.api.Assumptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class})
public abstract class TestBase {

    @Autowired
    private JaegerstaetterConfig config;

    @Autowired
    private ExistConnectionTestHelper connectionTestHelper;

    protected void testExistDBConnection() {
        Assumptions.assumeTrue(connectionTestHelper.connectionAvailable());
    }
}
