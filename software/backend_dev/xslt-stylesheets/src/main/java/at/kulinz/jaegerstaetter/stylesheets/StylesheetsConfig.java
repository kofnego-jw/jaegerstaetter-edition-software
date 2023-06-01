package at.kulinz.jaegerstaetter.stylesheets;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.xmlservice.XmlServiceConfig;
import at.kulinz.jaegerstaetter.xmlservice.service.SaxonExtensionProvider;
import at.kulinz.jaegerstaetter.xslt.extend.lucene.HighlightSaxonExtensionProvider;
import at.kulinz.jaegerstaetter.xslt.extend.lucene.HighlightingService;
import at.kulinz.jaegerstaetter.xslt.extend.lucene.HighlightingServiceImpl;
import at.kulinz.jaegerstaetter.xslt.extend.registry.KeyToNameSaxonExtensionProvider;
import at.kulinz.jaegerstaetter.xslt.extend.registry.KeyToNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.io.File;
import java.util.List;

@SpringBootConfiguration
@ComponentScan
@Import({JaegerstaetterConfig.class, XmlServiceConfig.class})
public class StylesheetsConfig {

    @Autowired
    private JaegerstaetterConfig jaegerstaetterConfig;

    @Bean
    public List<SaxonExtensionProvider> saxonExtensionProviderList() {
        HighlightingService highlightingService = new HighlightingServiceImpl();
        File registryJsonFile = jaegerstaetterConfig.getRegistryJsonFileDataRepository();
        KeyToNameService keyToNameService = new KeyToNameService(registryJsonFile);
        return List.of(new HighlightSaxonExtensionProvider(highlightingService),
                new KeyToNameSaxonExtensionProvider(keyToNameService));
    }

}
