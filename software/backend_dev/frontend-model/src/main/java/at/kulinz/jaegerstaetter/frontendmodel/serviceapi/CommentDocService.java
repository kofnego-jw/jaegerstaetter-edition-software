package at.kulinz.jaegerstaetter.frontendmodel.serviceapi;

import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.CommentDoc;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.MenuItem;

import java.io.FileNotFoundException;
import java.util.List;

public interface CommentDocService {

    /**
     * @return a list of all menu items for the "Digitale Edition" menu
     * @throws FrontendModelException when exception
     */
    List<MenuItem> getCommentDocMenu() throws FrontendModelException;

    /**
     * @return the comment doc of the start page item.
     * @throws FrontendModelException when exception
     */
    CommentDoc getStartPageCommentDoc() throws FrontendModelException;

    /**
     * Returns a comment doc of a given key, or throws a FileNotFoundException if not found
     *
     * @param resourceId the resourceId for the document. We use the filename of the tei file
     * @return a comment doc
     * @throws FileNotFoundException  if the comment doc cannot be found
     * @throws FrontendModelException if other exception happens
     */
    CommentDoc getCommentDoc(String resourceId) throws FileNotFoundException, FrontendModelException;

    /**
     * Returns a registry document for a given key
     *
     * @param registerType the type of registry, should be one of values in RegistryType
     * @return a comment doc
     * @throws FileNotFoundException  if no document can be found
     * @throws FrontendModelException if any other exception happens
     */
    CommentDoc getRegistryDoc(String registerType) throws FileNotFoundException, FrontendModelException;

    List<MenuItem> getMaterialMenu() throws FrontendModelException;

    CommentDoc getMaterialDoc(String key) throws FileNotFoundException, FrontendModelException;

    CommentDoc getBiographyIndex() throws FileNotFoundException, FrontendModelException;

    CommentDoc getContactCommentDoc() throws FileNotFoundException, FrontendModelException;

    CommentDoc getGdprCommentDoc() throws FileNotFoundException, FrontendModelException;

    CommentDoc getImprintCommentDoc() throws FileNotFoundException, FrontendModelException;

    CommentDoc getSpecialCorrespCommentDoc() throws FileNotFoundException, FrontendModelException;

    CommentDoc getAcknowledgementsCommentDoc() throws FileNotFoundException, FrontendModelException;

}
