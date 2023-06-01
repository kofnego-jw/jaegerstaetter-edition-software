package at.kulinz.jaegerstaetter.biography;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.stylesheets.StylesheetsConfig;
import at.kulinz.jaegerstaetter.tei.edition.connector.EditionConnectorConfig;
import at.kulinz.jaegerstaetter.tei.registry.RegistryConnectorConfig;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@Import({JaegerstaetterConfig.class, RegistryConnectorConfig.class, EditionConnectorConfig.class, StylesheetsConfig.class})
@ComponentScan
public class BiographyConfig {
}
