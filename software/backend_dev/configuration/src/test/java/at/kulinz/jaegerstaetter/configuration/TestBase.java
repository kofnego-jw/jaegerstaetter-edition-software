package at.kulinz.jaegerstaetter.configuration;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {JaegerstaetterConfig.class})
public abstract class TestBase {
}
