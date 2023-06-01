package at.kulinz.jaegerstaetter.pdfgenerator;

import at.kulinz.jaegerstaetter.stylesheets.StylesheetsConfig;
import com.openhtmltopdf.util.XRLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@ComponentScan
@Import({StylesheetsConfig.class})
public class PdfGeneratorConfig {
    static {
        XRLog.setLoggingEnabled(false);
    }

    @Value("${server.port}")
    String portNumberString;

    @Bean
    public String portNumber() {
        return this.portNumberString;
    }
}
