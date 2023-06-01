package at.kulinz.jaegerstaetter.configuration;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ConfigurationTest extends TestBase {
    @Autowired
    private JaegerstaetterConfig config;

    @Test
    public void test() throws Exception {
        Assertions.assertNotNull(config);
        System.out.println(config.editionLuceneDir().getCanonicalPath());
    }

}
