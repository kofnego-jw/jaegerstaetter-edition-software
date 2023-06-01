package at.kulinz.jaegerstaetter.tei.registry;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.search.SearchConfig;
import at.kulinz.jaegerstaetter.tei.edition.connector.EditionConnectorConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@Import({JaegerstaetterConfig.class, SearchConfig.class, EditionConnectorConfig.class})
@ComponentScan
public class RegistryConnectorConfig {


    @Autowired
    private JaegerstaetterConfig config;

}
