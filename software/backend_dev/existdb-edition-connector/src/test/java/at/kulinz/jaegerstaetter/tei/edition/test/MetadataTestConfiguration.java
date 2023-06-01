package at.kulinz.jaegerstaetter.tei.edition.test;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.stylesheets.service.TransformationServiceImpl;
import at.kulinz.jaegerstaetter.tei.edition.connector.impl.MetadataService;
import at.kulinz.jaegerstaetter.xmlservice.service.SaxonExtensionProvider;
import at.kulinz.jaegerstaetter.xmlservice.service.XsltService;
import at.kulinz.jaegerstaetter.xmlservice.service.impl.XsltServiceImpl;
import at.kulinz.jaegerstaetter.xslt.extend.lucene.HighlightSaxonExtensionProvider;
import at.kulinz.jaegerstaetter.xslt.extend.lucene.HighlightingService;
import at.kulinz.jaegerstaetter.xslt.extend.lucene.HighlightingServiceImpl;
import at.kulinz.jaegerstaetter.xslt.extend.registry.KeyToNameSaxonExtensionProvider;
import at.kulinz.jaegerstaetter.xslt.extend.registry.KeyToNameService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.io.File;
import java.util.List;

@SpringBootConfiguration
@Import({JaegerstaetterConfig.class})
public class MetadataTestConfiguration {

    @Autowired
    JaegerstaetterConfig jaegerstaetterConfig;

    @Bean
    public XsltService xsltService() {
        return new XsltServiceImpl();
    }

    public SaxonExtensionProvider keyToNameSaxonExtensionProvider() {
        File registryJsonFile = new File("../../../workingDir/repository/data.json");
        Assertions.assertTrue(registryJsonFile.exists());
        KeyToNameService service = new KeyToNameService(registryJsonFile);
        return new KeyToNameSaxonExtensionProvider(service);
    }

    public SaxonExtensionProvider highlightSaxonExtensionProvider() {
        HighlightingService highlightingService = new HighlightingServiceImpl();
        HighlightSaxonExtensionProvider provider = new HighlightSaxonExtensionProvider(highlightingService);
        return provider;
    }

    @Bean
    public TransformationServiceImpl transformationService() throws Exception {
        return new TransformationServiceImpl(
                List.of(keyToNameSaxonExtensionProvider(), highlightSaxonExtensionProvider()),
                new File("../xslt-stylesheets/src/main/resources/xsltstylesheets"), xsltService(), null, jaegerstaetterConfig);
    }

    @Bean
    public MetadataService metadataService() throws Exception {
        return new MetadataService(transformationService());
    }

}
