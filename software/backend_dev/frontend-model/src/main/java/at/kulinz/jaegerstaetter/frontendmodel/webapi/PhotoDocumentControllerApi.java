package at.kulinz.jaegerstaetter.frontendmodel.webapi;

import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.PhotoDocumentGroup;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/photodocument")
public interface PhotoDocumentControllerApi {

    @GetMapping("/")
    List<PhotoDocumentGroup> listAllGroupsAndItems();

    @GetMapping("/photo")
    List<PhotoDocumentGroup> listPhotoGroups();

    @GetMapping("/document")
    List<PhotoDocumentGroup> listDocumentGroups();

    @GetMapping("/{groupKey}/{id}")
    void getPhotoDocument(@PathVariable("groupKey") String groupKey, @PathVariable("id") String id, HttpServletResponse response) throws FileNotFoundException, FrontendModelException;


}
