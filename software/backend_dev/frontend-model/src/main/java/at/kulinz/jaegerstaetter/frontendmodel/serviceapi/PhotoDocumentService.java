package at.kulinz.jaegerstaetter.frontendmodel.serviceapi;

import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.PhotoDocumentGroup;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.PhotoDocumentItem;

import java.io.FileNotFoundException;
import java.util.List;

public interface PhotoDocumentService {

    List<PhotoDocumentGroup> listAllPhotoDocuments();

    List<PhotoDocumentItem> listByGroup(String groupKey) throws FileNotFoundException, FrontendModelException;

    List<PhotoDocumentGroup> listPhotoGroups();

    List<PhotoDocumentGroup> listDocumentGroups();

    byte[] getPhotoDocument(String groupKey, String id) throws FileNotFoundException, FrontendModelException;
}
