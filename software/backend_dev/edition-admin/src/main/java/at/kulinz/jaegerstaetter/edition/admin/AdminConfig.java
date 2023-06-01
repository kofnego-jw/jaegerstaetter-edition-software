package at.kulinz.jaegerstaetter.edition.admin;

import at.kulinz.jaegerstaetter.bibleregistry.BibleRegistryConfig;
import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.formaldesc.FormalDescConfig;
import at.kulinz.jaegerstaetter.photodoc.PhotoDocConfig;
import at.kulinz.jaegerstaetter.search.SearchConfig;
import at.kulinz.jaegerstaetter.tei.edition.connector.EditionConnectorConfig;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@ComponentScan
@Import({JaegerstaetterConfig.class, SearchConfig.class, EditionConnectorConfig.class, PhotoDocConfig.class, BibleRegistryConfig.class, FormalDescConfig.class})
public class AdminConfig {
}
