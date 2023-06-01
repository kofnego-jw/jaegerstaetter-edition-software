package at.kulinz.jaegerstaetter.edition.webapp.generic.web;

import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.PhotoDocumentGroup;
import at.kulinz.jaegerstaetter.frontendmodel.serviceapi.PhotoDocumentService;
import at.kulinz.jaegerstaetter.frontendmodel.webapi.PhotoDocumentControllerApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.net.URLConnection;
import java.util.List;

@RestController
public class PhotoDocumentController implements PhotoDocumentControllerApi {

    @Autowired
    private PhotoDocumentService photoDocumentService;

    @Override
    public List<PhotoDocumentGroup> listAllGroupsAndItems() {
        return photoDocumentService.listAllPhotoDocuments();
    }

    @Override
    public List<PhotoDocumentGroup> listPhotoGroups() {
        return photoDocumentService.listPhotoGroups();
    }

    @Override
    public List<PhotoDocumentGroup> listDocumentGroups() {
        return photoDocumentService.listDocumentGroups();
    }

    @Override
    public void getPhotoDocument(String groupKey, String id, HttpServletResponse response) throws FileNotFoundException, FrontendModelException {
        byte[] content = photoDocumentService.getPhotoDocument(groupKey, id);
        String mimetype = URLConnection.guessContentTypeFromName(id);
        ControllerHelper.sendContent(response, mimetype, content);
    }
}
