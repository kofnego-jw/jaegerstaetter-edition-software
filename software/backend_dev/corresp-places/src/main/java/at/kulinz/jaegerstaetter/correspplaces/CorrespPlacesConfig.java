package at.kulinz.jaegerstaetter.correspplaces;

import at.kulinz.jaegerstaetter.correspplaces.service.CorrespPlacesService;
import at.kulinz.jaegerstaetter.correspplaces.service.CorrespPlacesServiceImpl;
import at.kulinz.jaegerstaetter.stylesheets.StylesheetsConfig;
import at.kulinz.jaegerstaetter.xmlservice.service.XPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.io.File;

@SpringBootConfiguration
@ComponentScan
@Import({StylesheetsConfig.class})
public class CorrespPlacesConfig {

    public static final String PREVIEW_CORRESP_SAVE_LOCATION = "corresp/previewCorrespInfos.json";
    public static final String EDITION_CORRESP_SAVE_LOCATION = "corresp/editionCorrespInfos.json";

    @Autowired
    private File workingDir;

    @Autowired
    private XPathService xPathService;

    public File correspSavingLocation(String location) throws RuntimeException {
        File temp = new File(workingDir, location);
        if (!temp.exists() && !temp.getParentFile().exists() && !temp.getParentFile().mkdirs()) {
            throw new RuntimeException("Cannot create corresp save location file.");
        }
        return temp;
    }

    @Bean
    public CorrespPlacesService previewCorrespPlacesService() throws RuntimeException, CorrespPlacesException {
        return new CorrespPlacesServiceImpl(this.xPathService, correspSavingLocation(PREVIEW_CORRESP_SAVE_LOCATION));
    }

    @Bean
    public CorrespPlacesService editionCorrespPlacesService() throws RuntimeException, CorrespPlacesException {
        return new CorrespPlacesServiceImpl(this.xPathService, correspSavingLocation(EDITION_CORRESP_SAVE_LOCATION));
    }

}
