package at.kulinz.jaegerstaetter.testhelper;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@ComponentScan
@Import({JaegerstaetterConfig.class})
public class TestHelperConfig {
}
