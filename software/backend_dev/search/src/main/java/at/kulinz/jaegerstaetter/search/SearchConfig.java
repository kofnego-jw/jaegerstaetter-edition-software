package at.kulinz.jaegerstaetter.search;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.index.IndexConfig;
import at.kulinz.jaegerstaetter.xmlservice.XmlServiceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan
@Import({JaegerstaetterConfig.class, XmlServiceConfig.class, IndexConfig.class})
public class SearchConfig {

}
