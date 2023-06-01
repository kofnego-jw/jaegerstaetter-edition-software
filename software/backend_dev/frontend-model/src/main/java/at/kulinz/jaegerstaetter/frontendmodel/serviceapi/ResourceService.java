package at.kulinz.jaegerstaetter.frontendmodel.serviceapi;

import at.kulinz.jaegerstaetter.datamodel.model.TeiDocumentFW;
import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.*;

import java.io.FileNotFoundException;
import java.util.List;

public interface ResourceService {

    /**
     * Turn a TeiDocumentFW to ResourceFWDTO
     *
     * @param fw the TeiDocumentFW
     * @return a ResourceFWDTO
     */
    ResourceFWDTO fromTeiDocumentFW(TeiDocumentFW fw);

    /**
     * List all resources
     *
     * @return a list of ResourceFWDTO
     */
    List<ResourceFWDTO> listAllResources();

    /**
     * Returns a representation of the resource by a given resourceId
     *
     * @param resourceId the resourceId, this is usually the file path after the data directory
     * @param date       the date of retrieval, can be null
     * @return a ResourceFWDTO
     * @throws FileNotFoundException  if not found
     * @throws FrontendModelException if any other exception happens
     */
    ResourceFWDTO findResourceFWByIdAndDocumentId(String resourceId, String date) throws FrontendModelException, FileNotFoundException;

    /**
     * Returns a representation of the resource by a given resourceId
     *
     * @param resourceId the resourceId, this is usually the file path after the data directory
     * @return a ResourceFWDTO
     * @throws FileNotFoundException  if not found
     * @throws FrontendModelException if any other exception happens
     */
    ResourceFWDTO findLatestResourceFWByIdAndDocumentId(String resourceId) throws FrontendModelException, FileNotFoundException;

    /**
     * Find metadata and facsimiles of a given resource
     *
     * @param resourceId the id of the resource
     * @return a ResourceDTO
     * @throws FileNotFoundException if there is no resource with the id
     */
    ResourceDTO findResourceById(String resourceId, String date) throws FileNotFoundException, FrontendModelException;

    /**
     * Find metadata and facsimiles of a given resource
     *
     * @param resourceId the id of the resource
     * @return a ResourceDTO
     * @throws FileNotFoundException if there is no resource with the id
     */
    ResourceDTO findLatestResourceById(String resourceId) throws FileNotFoundException, FrontendModelException;

    /**
     * Returns the content of the file, without any transformation. Used for internal purposes.
     *
     * @param resourceId The id of the resource
     * @return the content of the file
     * @throws FileNotFoundException if no file is found.
     */
    byte[] getContent(String resourceId) throws FileNotFoundException;

    /**
     * Get the XML representation of a given resource
     *
     * @param resourceId    the id of the resource
     * @param includeHeader include TEI Header
     * @return a byte array of the XML data
     * @throws FileNotFoundException  if there is no resource with this id
     * @throws FrontendModelException if there is other exception
     */
    byte[] getXmlRepresentation(String resourceId, boolean includeHeader) throws FileNotFoundException, FrontendModelException;

    /**
     * Get the XML represetatnion of a given resource and a given time
     *
     * @param resourceId    the id of the resource
     * @param dating        the dating in format "yyyyMMddHHmm", could be null, then the most recent version should be returned
     * @param includeHeader include the teiHeader
     * @return a byte array of the XML data
     * @throws FileNotFoundException  if there is no resource with this id
     * @throws FrontendModelException if there is other exception
     */
    byte[] getXmlRepresentation(String resourceId, String dating, boolean includeHeader) throws FileNotFoundException, FrontendModelException;

    /**
     * Get the XML representation of a given resource and a given version
     *
     * @param resourceId the id of the resource
     * @param version    the version number, starting from 1, if null, the most recent version should be returned, if there is no version with the number, then the most recent version should be returened
     * @return a byte array of the XML data
     * @throws FileNotFoundException  if there is no resource with this id
     * @throws FrontendModelException if there is other exception
     */
    byte[] getXmlRepresentation(String resourceId, Integer version) throws FileNotFoundException, FrontendModelException;

    /**
     * Get the most recent normalized representation of a given resource
     *
     * @param resourceId the id of the resource
     * @return the HTML version of the normalized version of a given resource
     * @throws FileNotFoundException  if there is no resource with this id
     * @throws FrontendModelException if there is other exception
     */
    byte[] getHtmlNormalizedRepresentation(String resourceId) throws FileNotFoundException, FrontendModelException;

    /**
     * Get the normalized representation of a given resource at a given time
     *
     * @param resourceId the id of the resource
     * @param date       the dating in format "yyyyMMddHHmm", could be null, then the most recent version should be returned
     * @return the HTML version of the normalized version of a given resource
     * @throws FileNotFoundException  if there is no resource with this id
     * @throws FrontendModelException if there is other exception
     */
    byte[] getHtmlNormalizedRepresentation(String resourceId, String date) throws FileNotFoundException, FrontendModelException;

    /**
     * Get the normalized representation of a given resource at a given version
     *
     * @param resourceId the id of the resource
     * @param version    the version number, starting from 1, if null, the most recent version should be returned, if there is no version with the number, then the most recent version should be returened
     * @return the HTML version of the normalized version of a given resource
     * @throws FileNotFoundException  if there is no resource with this id
     * @throws FrontendModelException if there is other exception
     */
    byte[] getHtmlNormalizedRepresentation(String resourceId, Integer version) throws FileNotFoundException, FrontendModelException;

    /**
     * Get the normalized representatin of a given resource with places highlighted
     *
     * @param resourceId    the resource id
     * @param searchRequest the search request used for highlighting
     * @return the HTML version of the normalized version, search results highlighted
     * @throws FileNotFoundException  if no resource can be found
     * @throws FrontendModelException if any other exception happens
     */
    byte[] getHtmlNormalizedRepresentationWithHighlight(String resourceId, SearchRequest searchRequest) throws FileNotFoundException, FrontendModelException;


    /**
     * Get the normalized representatin of a given resource with rs with type and key highlighted
     *
     * @param resourceId    the resource id
     * @param markRsRequest the mark rs request used for highlighting
     * @return the HTML version of the normalized version, search results highlighted
     * @throws FileNotFoundException  if no resource can be found
     * @throws FrontendModelException if any other exception happens
     */
    byte[] getHtmlNormalizedRepresentationWithMarkRsRequest(String resourceId, MarkRsRequest markRsRequest) throws FileNotFoundException, FrontendModelException;

    /**
     * Get the most recent diplomatic representation of a given resource
     *
     * @param resourceId the id of the resource
     * @return the HTML version of the diplomatic version of a given resource
     * @throws FileNotFoundException  if there is no resource with this id
     * @throws FrontendModelException if there is other exception
     */
    byte[] getHtmlDiplomaticRepresentation(String resourceId) throws FileNotFoundException, FrontendModelException;

    /**
     * Get the diplomatic representation of a given resource at a given time
     *
     * @param resourceId the id of the resource
     * @param date       the dating in format "yyyyMMddHHmm", could be null, then the most recent version should be returned
     * @return the HTML version of the diplomatic version of a given resource
     * @throws FileNotFoundException  if there is no resource with this id
     * @throws FrontendModelException if there is other exception
     */
    byte[] getHtmlDiplomaticRepresentation(String resourceId, String date) throws FileNotFoundException, FrontendModelException;

    /**
     * Get the diplomatic representation of a given resource at a given version
     *
     * @param resourceId the id of the resource
     * @param version    the version number, starting from 1, if null, the most recent version should be returned, if there is no version with the number, then the most recent version should be returened
     * @return the HTML version of the diplomatic version of a given resource
     * @throws FileNotFoundException  if there is no resource with this id
     * @throws FrontendModelException if there is other exception
     */
    byte[] getHtmlDiplomaticRepresentation(String resourceId, Integer version) throws FileNotFoundException, FrontendModelException;

    /**
     * Get the normalized representation of the resource as PDF
     *
     * @param resourceId the resource ID
     * @return the PDF representation as a byte array
     * @throws FileNotFoundException  if the resource cannot be found
     * @throws FrontendModelException if any other exception happens
     */
    byte[] getPdfNormRepresentation(String resourceId) throws FileNotFoundException, FrontendModelException;

    /**
     * Get the facsimile as JPEG or PNG
     *
     * @param facResourceId the id of the facsimile
     * @return the bytearray containing the facsimile
     * @throws FileNotFoundException  if the fac cannot be found
     * @throws FrontendModelException if there is other exception
     */
    byte[] getFacsimile(String facResourceId) throws FileNotFoundException, FrontendModelException;

    /**
     * Gets a note
     *
     * @param resourceId the resource id
     * @param noteId     the id of the note
     * @param date       the date of desired access, used for versioninge
     * @return a NoteResourceDTO containing the note
     * @throws FileNotFoundException  if the resource or the note cannot be found
     * @throws FrontendModelException if any other exceptions happens
     */
    NoteResourceDTO getNoteResource(String resourceId, String noteId, String date) throws FileNotFoundException, FrontendModelException;

    /**
     * Gets a note
     *
     * @param resourceId the resource id
     * @param noteId     the id of the note
     * @return a NoteResourceDTO containing the note
     * @throws FileNotFoundException  if the resource or the note cannot be found
     * @throws FrontendModelException if any other exceptions happens
     */
    NoteResourceDTO getLatestNoteResource(String resourceId, String noteId) throws FileNotFoundException, FrontendModelException;

    /**
     * Returns information on corresponding places in the edition as a CorrespInfo
     *
     * @param resourceId the resourceId of the resource
     * @param anchorName the anchor of the place of the corresponding place
     * @return a CorrespInfo, it could be empty if there are no corresponding places
     */
    CorrespInfo getCorrespInfo(String resourceId, String anchorName);

}
