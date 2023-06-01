package at.kulinz.jaegerstaetter.formaldesc;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.tei.edition.connector.EditionConnectorConfig;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@Import({JaegerstaetterConfig.class, EditionConnectorConfig.class})
@ComponentScan
public class FormalDescConfig {
}
