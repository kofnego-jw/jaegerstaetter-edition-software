package at.kulinz.jaegerstaetter.tei.edition.connector;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.correspplaces.CorrespPlacesConfig;
import at.kulinz.jaegerstaetter.pdfgenerator.PdfGeneratorConfig;
import at.kulinz.jaegerstaetter.stylesheets.StylesheetsConfig;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@ComponentScan
@Import({JaegerstaetterConfig.class, StylesheetsConfig.class, PdfGeneratorConfig.class, CorrespPlacesConfig.class})
public class EditionConnectorConfig {

}
