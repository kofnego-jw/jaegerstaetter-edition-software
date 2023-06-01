package at.kulinz.jaegerstaetter.edition.webapp.generic.web;

import at.kulinz.jaegerstaetter.biography.service.EditionBiographyService;
import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.Biography;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.BiographyFW;
import at.kulinz.jaegerstaetter.frontendmodel.webapi.BiographyControllerApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.net.URLConnection;
import java.util.List;

@RestController
@Profile("edition")
public class EditionBiographyController implements BiographyControllerApi {

    @Autowired
    private EditionBiographyService biographyService;

    @Override
    public List<BiographyFW> listBiographies() {
        return biographyService.listBiographies();
    }

    @Override
    public Biography getBiography(String personKey) throws FileNotFoundException, FrontendModelException {
        return biographyService.getBiography(personKey);
    }

    @Override
    public void getImage(String filename, HttpServletResponse response) throws FrontendModelException, FileNotFoundException {
        byte[] content = biographyService.getImage(filename);
        String mimetype = URLConnection.guessContentTypeFromName(filename);
        ControllerHelper.sendContent(response, mimetype, content);
    }

    @Override
    public void getPdf(String filename, HttpServletResponse response) throws FileNotFoundException, FrontendModelException {
        byte[] content = biographyService.getPdf(filename);
        String mimetype = URLConnection.guessContentTypeFromName("test.pdf");
        ControllerHelper.sendContent(response, mimetype, content);
    }

}
