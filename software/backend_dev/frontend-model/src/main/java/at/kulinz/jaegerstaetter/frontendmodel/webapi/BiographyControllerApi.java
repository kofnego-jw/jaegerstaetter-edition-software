package at.kulinz.jaegerstaetter.frontendmodel.webapi;

import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.Biography;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.BiographyFW;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/biography")
public interface BiographyControllerApi {

    @GetMapping("/")
    List<BiographyFW> listBiographies();

    @GetMapping("/{personKey}")
    Biography getBiography(@PathVariable("personKey") String personKey) throws FileNotFoundException, FrontendModelException;

    @GetMapping("/images/{filename}")
    void getImage(@PathVariable("filename") String filename, HttpServletResponse response) throws FileNotFoundException, FrontendModelException;

    @GetMapping("/pdf/{filename}")
    void getPdf(@PathVariable("filename") String filename, HttpServletResponse response) throws FileNotFoundException, FrontendModelException;
}
