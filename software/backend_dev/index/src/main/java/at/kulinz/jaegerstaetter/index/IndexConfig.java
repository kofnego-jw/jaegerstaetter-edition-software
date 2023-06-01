package at.kulinz.jaegerstaetter.index;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@Import({JaegerstaetterConfig.class})
@ComponentScan()
public class IndexConfig {
}
