package at.kulinz.jaegerstaetter.datamodel.repository;

import at.kulinz.jaegerstaetter.datamodel.DataModelException;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocumentFW;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TeiRepository {

    TeiDocument saveUnderUrl(TeiDocument document, String url) throws DataModelException;

    TeiDocument createNewDocument(String filePath, byte[] content, LocalDateTime timestamp) throws DataModelException;

    TeiDocument addNewVersion(TeiDocument oldVersion, TeiDocument newVersion) throws DataModelException;

    Optional<TeiDocument> retrieveByUrl(String url);

    /**
     * Returns the newest version of the TeiDocument using the filePath
     *
     * @param filePath the path under which the document is to be found
     * @return an Optional of the TeiDocument
     */
    Optional<TeiDocument> retrieveByFilePath(String filePath);

    /**
     * Returns the given version of the document under the filePath.
     *
     * @param filePath the filepath
     * @param version  the version
     * @return an Optional of TeiDocument
     */
    Optional<TeiDocument> retrieveByFilePathAndVersion(String filePath, Integer version);

    /**
     * Get all versions of a TeiDocument under a filePath
     *
     * @param filePath the FilePath
     * @return a list of all versions, starting with the newest
     */
    List<TeiDocument> retrieveAllVersions(String filePath);

    default Optional<TeiDocument> retrieveByFilePathAndTimestamp(String filePath, LocalDateTime timestamp) {
        if (timestamp == null) {
            return retrieveByFilePath(filePath);
        }
        List<TeiDocument> list = retrieveAllVersions(filePath);
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return list.stream().filter(doc -> !timestamp.isBefore(doc.getCreationTimestamp()))
                .findFirst();
    }

    /**
     * Retrieve a list of contentes within the filePath
     *
     * @param filePath  the file path of the directory
     * @param recursive should traverse recursively
     * @return a List of TeiDocumentFW
     */
    List<TeiDocumentFW> retrieveAllUnder(String filePath, boolean recursive);

    /**
     * Returns all undeleted TeiDocument
     *
     * @return a list of TeiDocument
     */
    List<TeiDocumentFW> retrieveAll();
}
